package br.com.ggdio.security.domain.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ggdio.security.domain.dto.Login;
import br.com.ggdio.security.domain.exception.AuthenticationException;
import br.com.ggdio.security.domain.exception.AuthorizationException;
import br.com.ggdio.security.domain.exception.Error;
import br.com.ggdio.security.domain.exception.ExpiredRefreshWindowException;
import br.com.ggdio.security.domain.exception.InvalidTokenException;
import br.com.ggdio.security.domain.model.AuthorizationType;
import br.com.ggdio.security.domain.model.DestroyReason;
import br.com.ggdio.security.domain.model.Group;
import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.security.domain.model.User;
import br.com.ggdio.security.domain.model.ldap.LDAPData;
import br.com.ggdio.security.domain.repository.GroupRepository;
import br.com.ggdio.security.domain.repository.SessionRepository;
import br.com.ggdio.security.domain.repository.UserRepository;
import br.com.ggdio.security.domain.repository.ldap.LDAPRepository;
import br.com.ggdio.specs.common.ComponentRegistry;
import br.com.ggdio.specs.domain.exception.DomainException;
import br.com.ggdio.specs.infrastructure.exception.InfrastructureException;
import br.com.ggdio.specs.infrastructure.repository.TimestampRepository;

/**
 * Cross domain service for user authentication
 * 
 * @author Guilherme Dio
 *
 */
public class LoginService {

	private final ComponentRegistry registry;
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);
	
	private Group dataOwnerGroup;
	
	public LoginService(ComponentRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * Authenticates users
	 * 
	 * @param username - Login
	 * @param password - Password/Secret
	 * @return {@link Session}
	 * @throws AuthenticationException - Wrong login data
	 * 
	 */
	public Session login(String username, String password) throws AuthenticationException {
		return login(null, username, password);
	}
	
	/**
	 * Authenticates users
	 * 
	 * @param domain - User domain name
	 * @param username - Login
	 * @param password - Password/Secret
	 * @return {@link Session}
	 * @throws AuthenticationException - Wrong login data
	 */
	public Session login(String domain, String username, String password) throws AuthenticationException{
		return login(new Login(domain, username, password));
	}
	
	/**
	 * Authenticates users
	 * 
	 * @param login - Login DTO
	 * @return {@link Session}
	 * @throws AuthenticationException - Wrong login data
	 * 
	 */
	public Session login(Login login) throws AuthenticationException {
		if(login == null) throw new AuthenticationException(Error.LOGIN_NOT_NULL);
		
		LDAPRepository ldapRepo = registry.lookup(LDAPRepository.class);
		UserRepository userRepo = registry.lookup(UserRepository.class);
		GroupRepository groupRepo = registry.lookup(GroupRepository.class);
		SessionRepository sessionRepo = registry.lookup(SessionRepository.class);
		TimestampRepository timestampRepo = registry.lookup(TimestampRepository.class);
		
		LDAPData data;
		try {
			// queries LDAP w/ user+pass
			data = ldapRepo.authenticate(login.getUsername(), login.getPassword(), login.getDomain());
			if(data == null) { // null means invalid user
				throw new AuthenticationException(Error.INVALID_CREDENTIALS);
			}
			
		} catch(InfrastructureException e) { // something went wront at ldap
			throw new AuthenticationException(Error.AUTHENTICATION_PROBLEMS, e, e.getMessage());
			
		}
		
		// groups from current user
		Set<Group> groups = groupRepo.findByKeys(data.getGroups());
		
		if((groups == null || groups.isEmpty())) { // access forbidden
			throw new AuthorizationException(Error.USER_LACKS_ROLES, login.getUsername());
		}
			
		User user = userRepo.findByKey(data.getsAMAccountName().toLowerCase());
		long currentTimestamp = timestampRepo.currentTimestamp();
		LocalDateTime now = Instant.ofEpochMilli(currentTimestamp)
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		
		if(user == null) { // NEW USER FLOW
			user = new User(null, data.getsAMAccountName(), now);
			user.setName(data.getDisplayName());
			user.setEmail(data.getMail());
			user.setLastLogin(now);
			userRepo.save(user);
			
		} else { // RECURRENT USER FLOW
			Session session = sessionRepo.findActiveByUser(user.getId());
			if(session != null) {
				if(session.isAlive()) {
					return session; // session still alive, re-use it
					
				} else {
					if(!session.isRefreshWindowActive()) { // refresh windows must be over so we can EXPIRE session completely
						session.destroy(DestroyReason.EXPIRED); // destroy expired session and set reason to EXPIRED
						sessionRepo.save(session);
						
					} else {
						session.refreshToken(generateRefreshToken()); // refresh existing session with refresh token
						sessionRepo.save(session);
						return session;
						
					}
					
				}
			}
			user.setLastLogin(now); // update last login
			userRepo.save(user);
			
		}
		
		final String token = generateToken();
		final String refreshToken = generateRefreshToken();
		
		// build a new session
		Session session = Session.builder(timestampRepo)
			.authType(AuthorizationType.OAUTH)
			.token(refreshToken, token)
			.groups(groups)
			.user(user)
			.userAgent(login.getUserAgent())
			.host(login.getHost())
			.ip(login.getIp())
			.createdAt(now)
			.lastToken(now)
			.build();
		
		sessionRepo.save(session);
		
		return session;
	}

	/**
	 * Validates a given token for an alive session
	 * 
	 * @param token - Key for session
	 * 
	 * @return Existing {@link Session} or NULL if not alive
	 */
	public Session validate(String token) {
		if(token == null) throw new InvalidTokenException("NULL");
		
		SessionRepository sessionRepo = registry.lookup(SessionRepository.class);
		
		// check for existing not-destroyed session
		Session session = sessionRepo.findActiveByToken(token);
		if(session == null) {
			throw new InvalidTokenException(token);
		}
		
		if(!session.isAlive()) { // check liveness
			
			if(!session.isRefreshWindowActive()) { // if refresh window isn't active, then expire it
				destroySession(sessionRepo, session, DestroyReason.EXPIRED);
			}
			
			throw new InvalidTokenException(token);
		}
		
		return session;
	}
	
	/**
	 * Refreshes a session w/ it's refresh token
	 * @param refreshToken - The refresh token
	 * @return A refreshed {@link Session} with a new token (refresh is the same)
	 */
	public Session refresh(String refreshToken) {
		if(refreshToken == null) throw new InvalidTokenException("NULL");
		
		SessionRepository sessionRepo = registry.lookup(SessionRepository.class);
		
		Session session = sessionRepo.findActiveByRefreshToken(refreshToken);
		if(session == null) {
			throw new InvalidTokenException(refreshToken);
		}
		
		try {
			session.refreshToken(generateRefreshToken());
			sessionRepo.save(session);
			
		} catch(ExpiredRefreshWindowException e) {
			destroySession(sessionRepo, session, DestroyReason.EXPIRED);
			throw e;
		}
		
		return session;
	}

	public void destroy(String token) {
		SessionRepository sessionRepo = registry.lookup(SessionRepository.class);
		
		Session session = sessionRepo.findActiveByToken(token);
		if(session != null) {
			destroySession(sessionRepo, session, DestroyReason.LOGOUT);
		}
	}
	
	private void destroySession(SessionRepository sessionRepo, Session session, DestroyReason reason) {
		session.destroy(reason);
		sessionRepo.save(session);
//		sessionRepo.clearGroups(session); TODO: Create a flow for clearing older sessions groups relations only 
	}
	
	/**
	 * Generates a unique string session token
	 * @return A token as a {@link String}
	 */
	private String generateToken() {
		return generateUUID(0, registry.lookup(SessionRepository.class)::findActiveByToken);
	}

	/**
	 * Generates a unique string refresh token
	 * @return A token as a {@link String}
	 */
	private String generateRefreshToken() {
		return generateUUID(0, registry.lookup(SessionRepository.class)::findActiveByRefreshToken);
	}

	/**
	 * Generates an UUID and validates it by using a {@link Function}
	 * @param attempt - Current generation attempt (max 3)
	 * @return A token as a {@link String}
	 */
	private String generateUUID(int attempt, Function<String, Session> checkFunction) {
		if(attempt == 3) throw new DomainException("Cannot generate token for session.");
		
		String token = UUID.randomUUID().toString();
		if(checkFunction.apply(token) != null) {
			token = generateUUID(++attempt, checkFunction);
		}
		
		return token;
	}
	
	
}