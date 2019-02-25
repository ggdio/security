package br.com.ggdio.rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitPublisher {

	private static final String QUEUE_NAME = "security";
	private static Channel channel;

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		
		publish();
	}

	private static void publish() throws IOException {
		String message = getMessage();
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
	}

	private static String getMessage() {
		return "{"
				+ "\"eventId\":\"security.app.created\","
				+ "\"data\": {"
					+ "\"appKey\": \"my.new.app\","
					+ "\"appDescription\":\"My New App\""
				+ "}"
			 + "}";
	}
	
}
