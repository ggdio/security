package br.com.ggdio.specs.event;

/**
 * A simple API for distributed event producing
 * 
 * @author Guilherme Dio
 *
 */
public class EventProducer extends EventHandler {
	
	private final String senderId;
	
	public EventProducer(String senderId, String host) {
		this(senderId, host, 0);
	}
	
	public EventProducer(String senderId, String host, int port) {
		this(senderId, host, port, null, null);
	}

	public EventProducer(String senderId, String host, int port, String username, String password) {
		super(host, port, username, password);
		
		this.senderId = senderId;
	}
	
	/**
	 * Raises a new event in the system
	 * @param id - Event ID
	 * @param data - Event Data
	 * @see EventData
	 */
	public void raiseEvent(String id, EventData data) {
		super.publish(Event.builder()
				.id(id)
				.senderId(senderId)
				.data(data)
				.build());
	}
	
}