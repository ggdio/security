package br.com.ggdio.event.receiver;

import br.com.ggdio.specs.event.EventReceiver;

public class EventReceiverTest {

	public static void main(String[] args) {
		EventReceiver receiver = new EventReceiver("localhost");
		
		receiver.declareEventId("newApp", "security.newapp");
		NewAppEventListener listener = new NewAppEventListener();
		receiver.registerListener(listener);
	}

}
