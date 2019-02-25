package br.com.ggdio.event.producer;

import br.com.ggdio.specs.event.EventProducer;

public class EventProducerTest {

	public static void main(String[] args) {
		EventProducer producer = new EventProducer("specs.test", "localhost");
		
		NewAppEventData data = new NewAppEventData();
		data.setAppKey("bcl");
		data.setAppName("BCL");
		data.setTemplateKey("default");
		
		producer.declareEventId("newApp", "security.newapp");
		producer.raiseEvent("newApp", data);
		producer.close();
	}
	
}
