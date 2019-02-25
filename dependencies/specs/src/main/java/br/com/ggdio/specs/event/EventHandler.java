package br.com.ggdio.specs.event;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import br.com.ggdio.json.JSONUtils;
import br.com.ggdio.specs.event.Event.EventBuilder;
import br.com.ggdio.specs.event.exception.EventConnectionException;

/**
 * Event handling(producer vs. receiver) abstraction 
 * 
 * @author Guilherme Dio
 *
 */
public abstract class EventHandler {
	
	protected final Channel channel;
	private final Connection connection;
	
	private final Map<String, String> eventPerQueue = new HashMap<>();


	public EventHandler(String host, int port, String username, String password) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(host);
			if(port > 0) factory.setPort(port);
			if(username != null) {
				factory.setUsername(username);
				factory.setPassword(password);
			}
			factory.setConnectionTimeout(60000);
			factory.setAutomaticRecoveryEnabled(true);
			connection = factory.newConnection();
			channel = connection.createChannel();
			
		} catch(Exception e) {
			throw new EventConnectionException("Error while connecting to rabbitMQ due to '"+e.getMessage()+"'", e);
			
		}
	}
	
	public void declareEventId(String eventId, String queueName) {
		if(eventId == null || queueName == null) throw new IllegalArgumentException("eventId and queueName cannot be null.");
		try {
			eventPerQueue.put(eventId, queueName);
			channel.queueDeclare(queueName, true, false, false, null);
			
		} catch (IOException e) {
			throw new EventConnectionException("Error while declaring a queue in rabbitMQ due to '"+e.getMessage()+"'", e);
			
		}
	}
	
	protected void publish(Event event) {
		String queue = getQueue(event.getId());
		if(queue == null) throw new IllegalStateException("No queue registered for event '"+event.getId()+"'");
		
		try {
			channel.basicPublish("", queue, null, getMessage(event));
			
		} catch (IOException e) {
			throw new EventConnectionException("Error while publishing a message in rabbitMQ due to '"+e.getMessage()+"'", e);
			
		}
	}
	
	protected String getQueue(String eventId) {
		return eventPerQueue.get(eventId);
	}
	
	protected byte[] getMessage(Event event) {
		Map<String, Object> data = new HashMap<>();
		data.put("eventId", event.getId());
		data.put("senderId", event.getSenderId());
		data.put("data", event.getData());
		return JSONUtils.toJSON(data).getBytes(Charset.forName("UTF-8"));
	}
	
	@SuppressWarnings("unchecked")
	protected Event getEvent(Class<? extends EventData> serialize, String message) {
		Map<String, Object> data = (Map<String, Object>) JSONUtils.fromJSON(message);
		
		String id = (String) data.get("eventId");
		String sender = (String) data.get("senderId");
		Map<String, Object> eventData = (Map<String, Object>) data.get("data");
		
		EventBuilder builder = Event.builder().id(id).senderId(sender);
		if(serialize != null) {
			//TODO: FIX IT ASAP !
			builder.data(JSONUtils.fromJSON(serialize, JSONUtils.toJSON(eventData)));
			
		} else {
			builder.data(new MappedEventData(eventData));
			
		}
		
		return builder.build();
	}
	
	public boolean isOpen() {
		return channel.isOpen();
	}
	
	public void close() {
		try {
			channel.abort();
			connection.close();
			
		} catch (IOException e) {
			throw new EventConnectionException("Error while closing the channel due to '"+e.getMessage()+"'", e);
			
		}
	}
	
	
}