package br.com.ggdio.security.application.event;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ggdio.security.AppEventData;
import br.com.ggdio.security.domain.service.SecurityService;
import br.com.ggdio.specs.domain.exception.DomainException;
import br.com.ggdio.specs.event.EventListener;

/**
 * Event listener for App events
 * 
 * @author Guilherme Dio
 *
 */
public class AppEventListener implements EventListener<AppEventData> {
	
	public static final String QUEUE = "security.newapp";
	public static final String EVENT_ID = "event.security.newapp";
	
	//TODO: It should be dynamic
	private static final Set<String> ALLOWED_SENDERS = new HashSet<String>();
	
	private static final Logger LOG = LoggerFactory.getLogger(AppEventListener.class);
	
	private final SecurityService service;
	
	public AppEventListener(SecurityService service) {
		this.service = service;
		ALLOWED_SENDERS.add("microservice.catalog");
		ALLOWED_SENDERS.add("microservice.workflow");
		ALLOWED_SENDERS.add("microservice.mailing");
		ALLOWED_SENDERS.add("microservice.menu");

	}

	@Override
	public String getEventId() {
		return EVENT_ID;
	}

	@Override
	public void eventReceived(String senderId, AppEventData data) {
		if(senderId == null || !ALLOWED_SENDERS.contains(senderId)) return;
		
		LOG.info("New Event Received [eventId={}, senderId={}, appKey={}, userKey={}]", getEventId(), senderId, data.getAppKey(), data.getUserId());
		
		String appKey = data.getAppKey();
		String appName = data.getAppName();
		String templateKey = data.getTemplateKey();
		String userKey = data.getUserId();
		
		try {
			service.secureApp(appKey, appName, templateKey, userKey);
			
			// TODO: Produce status data with success
			
		} catch(DomainException e) {
			LOG.error("Something went wrong while securing new app.", e);
			
			// TODO: Produce status data with error
			
		}
	}

}