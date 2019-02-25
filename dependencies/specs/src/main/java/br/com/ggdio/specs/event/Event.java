package br.com.ggdio.specs.event;

class Event {

	private final String id;
	private final String senderId;
	private final EventData data;
	
	public Event(String id, String senderId, EventData data) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public String getSenderId() {
		return senderId;
	}

	public EventData getData() {
		return data;
	}
	
	static EventBuilder builder() {
		return new EventBuilder();
	}
	
	static class EventBuilder {
		
		private String id;
		private String senderId;
		private EventData data;
		
		public EventBuilder id(String id) {
			this.id = id;
			return this;
		}
		
		public EventBuilder senderId(String senderId) {
			this.senderId = senderId;
			return this;
		}
		
		public EventBuilder data(EventData data) {
			this.data = data;
			return this;
		}
		
		public Event build() {
			return new Event(id, senderId, data);
		}
		
	}
	
}