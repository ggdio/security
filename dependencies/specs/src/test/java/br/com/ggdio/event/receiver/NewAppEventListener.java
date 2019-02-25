package br.com.ggdio.event.receiver;

import br.com.ggdio.event.producer.NewAppEventData;
import br.com.ggdio.specs.event.EventListener;

public class NewAppEventListener implements EventListener<NewAppEventData> {

	@Override
	public String getEventId() {
		return "newApp";
	}

	@Override
	public void eventReceived(String senderId, NewAppEventData data) {
		System.out.println("AppKey = " + data.getAppKey());
		System.out.println("AppName = " + data.getAppName());
	}

}
