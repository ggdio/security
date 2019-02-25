package br.com.ggdio.specs.event;

/**
 * Listener interface for receiving events
 * 
 * @author Guilherme Dio
 *
 */
public interface EventListener<T extends EventData> {

	public String getEventId();
	
	public void eventReceived(String senderId, T data);
	
}