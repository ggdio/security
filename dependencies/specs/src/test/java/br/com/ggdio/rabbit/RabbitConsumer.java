package br.com.ggdio.rabbit;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import br.com.ggdio.json.JSONUtils;

public class RabbitConsumer {

	private static final String QUEUE_NAME = "security.newapp";
	private static Channel channel;

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		consume();
	}

	private static void consume() throws IOException {
		Consumer consumer = new DefaultConsumer(channel) {
			@Override @SuppressWarnings("unchecked")
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				
				Map<String, Object> event = JSONUtils.fromJSON(Map.class, message);
				Map<String, Object> data = (Map<String, Object>) event.get("data");
				
				System.out.println("\t[x] Received '" + message + "'");
				System.out.println("\t\t[x] EventID '" + event.get("eventId") + "'");
				System.out.println("\t\t[x] AppID '" + data.get("appKey") + "'");
				System.out.println("\t\t[x] AppDescription '" + data.get("appDescription") + "'");
				
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}

}
