package br.com.ggdio.specs.event;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.com.ggdio.specs.event.exception.EventConnectionException;

/**
 * A simple API for distributed event receiving
 * 
 * @author Guilherme Dio
 *
 */
public class EventReceiver extends EventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventReceiver.class);
	
	public EventReceiver(String host) {
		this(host, 0);
	}
	
	public EventReceiver(String host, int port) {
		this(host, port, null, null);
	}

	public EventReceiver(String host, int port, String username, String password) {
		super(host, port, username, password);
	}
	
	/**
	 * Registers a new event listener
	 * 
	 * @param eventId
	 * @param listener
	 */
	@SuppressWarnings("unchecked")
	public <T extends EventData> void registerListener(EventListener<T> listener) {
		final Class<T> type = getEntityClazz(listener);
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				boolean ok = false;
				try {
					String message = new String(body, Charset.forName("UTF-8"));
					Event event = getEvent(type, message);
					
					if(LOG.isDebugEnabled()) {
						LOG.debug("EVENT received [eventId={}, senderId={}, dataType={}, message={}]", event.getId(), event.getSenderId(), event.getData().getClass(), message);
						
					} else {
						LOG.info("EVENT received [eventId={}, senderId={}, dataType={}]", event.getId(), event.getSenderId(), event.getData().getClass());
						
					}
					
					listener.eventReceived(event.getSenderId(), (T) event.getData());
					
					ok = true;
					
				} catch(Exception e) {
					LOG.error("Failed to receive event due to '"+e.getMessage()+"'", e);
					
				} finally {
					if(ok) {
						channel.basicAck(envelope.getDeliveryTag(), false);
					}
				}
			}
		};
		
		try {
			channel.basicConsume(getQueue(listener.getEventId()), false, consumer);
			
		} catch (IOException e) {
			String msg = "Failed to init a new consumer due to '"+e.getMessage()+"'";
			LOG.error(msg, e);
			
			throw new EventConnectionException(msg, e);
			
		}
	}
	
	@SuppressWarnings("unchecked")
    public <T extends EventData> Class<T> getEntityClazz(EventListener<T> listener) {
		ParameterizedType pt = null;
		Type[] interfaces = listener.getClass().getGenericInterfaces();
		for(Type itf : interfaces) {
			if(itf.getTypeName().contains(EventListener.class.getName())) {
				pt = (ParameterizedType) itf;
			}
		}
		if(pt == null) throw new IllegalStateException("Parameterized listener '"+listener.getClass()+"' does not define an event data type");
		
        String clazz = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        
        try {
        	return (Class<T>) Class.forName(clazz);
        } catch(Exception e) {
        	throw new RuntimeException(e);
        }
    }

}