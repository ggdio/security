package br.com.ggdio.event.producer;

import br.com.ggdio.security.AppEventData;
import br.com.ggdio.specs.event.EventProducer;

public class EventProducerTest {
	
	private static final String SENDER_ID = "microservice.catalog";
	private static final String EVENT_ID = "event.security.newapp";
	private static final String QUEUE = "security.newapp";

	public static void main(String[] args) {
		EventProducer producer = new EventProducer(SENDER_ID, "localhost");
		
		AppEventData data = new AppEventData();
		data.setAppKey("telemetry");
		data.setAppName("Telemetry");
		data.setTemplateKey("default");
		data.setUserId("t707723");
		
		producer.declareEventId(EVENT_ID, QUEUE);
		producer.raiseEvent(EVENT_ID, data);
		producer.close();
	}
	
}
