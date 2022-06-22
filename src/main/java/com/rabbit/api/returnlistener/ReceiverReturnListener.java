package com.rabbit.api.returnlistener;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 11:37 2022/5/19
 */
@Slf4j
public class ReceiverReturnListener {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setNetworkRecoveryInterval(3000);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_return_listener_exchange";
        String queueName = "test_return_listener_queue";
        String routingKey = "return.#";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, false, false,
                false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到的消息==:{}",new String(body));
            }
        };
        channel.basicConsume(queueName, true, "return消息", consumer);

    }
}
