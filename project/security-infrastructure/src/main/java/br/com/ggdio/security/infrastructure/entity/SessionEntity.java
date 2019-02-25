package br.com.ggdio.security.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ggdio.security.domain.model.Session;
import br.com.ggdio.security.domain.model.Session.NewSessionBuilder;
import br.com.ggdio.specs.common.ComponentRegistry;
import br.com.ggdio.specs.infrastructure.converter.BooleanToCharConverter;
import br.com.ggdio.specs.infrastructure.converter.LocalDateTimeConverter;
import br.com.ggdio.specs.infrastructure.entity.AbstractEntity;
import br.com.ggdio.specs.infrastructure.repository.TimestampRepository;

@Entity
@Table(name="TB_SESSION", indexes= {@Index(columnList="DS_TOKEN", unique=true), @Index(columnList="ID_USER")})
public class SessionEntity extends AbstractEntity<Session> {
	
	@Id
	@Column(name="DS_REFRESH", updatable=false)
	private String refreshToken;
	
	@Column(name="DS_TOKEN", nullable=false)
	private String token;
	
	@Column(name="DO_AUTHTYPE")
	@Enumerated(EnumType.STRING)
	private AuthorizationTypeEntity authType;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "TB_SESSION_GROUP",
	    joinColumns = @JoinColumn(name = "DS_TOKEN"),
	    inverseJoinColumns = @JoinColumn(name = "ID_GROUP")
	)
	private Set<GroupEntity> groups;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_USER", nullable=false)
	private UserEntity user;
	
	@Column(name="DS_USERAGENT")
	private String userAgent;

	@Column(name="DS_HOST")
	private String host;
	
	@Column(name="DS_IP")
	private String ip;
	
	@Column(name="DO_DESTROYED")
	@Convert(converter=BooleanToCharConverter.class)
	private Boolean destroyed;
	
	@Column(name="DO_DESTROYREASON")
	@Enumerated(EnumType.STRING)
	private DestroyReasonEntity destroyReason;
	
	@Column(name="DT_CREATED", updatable=false)
    @Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime createdAt;
	
	@Column(name="DT_LASTTOKEN")
    @Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime lastToken;
	
	@Column(name="DT_DESTROYED")
    @Convert(converter=LocalDateTimeConverter.class)
	private LocalDateTime destroyedAt;
	
	public SessionEntity() {
		
	}
	
	public SessionEntity(Session domain) {
		this.refreshToken = domain.getRefreshToken();
		this.token = domain.getToken();
		this.authType = AuthorizationTypeEntity.wrap(domain.getAuthType());
		
		this.groups = new HashSet<>();
		domain.getGroups().forEach(g -> groups.add(new GroupEntity(g)));
		
		this.user = new UserEntity(domain.getUser());
		this.userAgent = domain.getUserAgent();
		this.host = domain.getHost();
		this.ip = domain.getIp();
		
		if(domain.getDestroyReason() != null) {
			this.destroyReason = DestroyReasonEntity.wrap(domain.getDestroyReason());
		}
		
		this.lastToken = domain.getLastToken();
		
		this.destroyed = domain.isDestroyed();
		this.destroyedAt = domain.getDestroyedAt();
		
		this.createdAt = domain.getCreatedAt();
	}

	@Override
	public String getId() {
		return getRefreshToken();
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public String getToken() {
		return token;
	}
	
	public AuthorizationTypeEntity getAuthType() {
		return authType;
	}

	public Set<GroupEntity> getGroups() {
		return new HashSet<>(groups);
	}

	public UserEntity getUser() {
		return user;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getHost() {
		return host;
	}

	public String getIp() {
		return ip;
	}

	public Boolean getDestroyed() {
		return destroyed;
	}
	
	public DestroyReasonEntity getDestroyReason() {
		return destroyReason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	public LocalDateTime getLastToken() {
		return lastToken;
	}

	public LocalDateTime getDestroyedAt() {
		return destroyedAt;
	}
	
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public void setAuthType(AuthorizationTypeEntity authType) {
		this.authType = authType;
	}

	public void setGroups(Set<GroupEntity> groups) {
		this.groups = new HashSet<>(groups);
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setDestroyed(Boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	public void setDestroyReason(DestroyReasonEntity destroyReason) {
		this.destroyReason = destroyReason;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public void setLastToken(LocalDateTime lastToken) {
		this.lastToken = lastToken;
	}

	public void setDestroyedAt(LocalDateTime destroyedAt) {
		this.destroyedAt = destroyedAt;
	}
	
	@Override
	public Session unwrap() {
		throw new Error("SessionEntity.unwrap() should never be invoked ! Use SessionEntity.unwrap(ComponentRegistry) instead.");
	}

	@Override
	public Session unwrap(ComponentRegistry registry) {
		NewSessionBuilder builder = Session.builder(registry.lookup(TimestampRepository.class));
		getGroups().forEach(g -> builder.groups(g.unwrap()));
		
		if(getDestroyed()) {
			builder.destroyed(getDestroyReason().unwrap(), getDestroyedAt());
		}
		
		return builder
				.authType(getAuthType().unwrap())
				.token(getRefreshToken(), getToken())
				.user(getUser().unwrap())
				.userAgent(getUserAgent())
				.host(getHost())
				.ip(getIp())
				.createdAt(getCreatedAt())
				.lastToken(getLastToken())
				.build();
	}

}
