package com.rabbit.api.directexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ym.y
 * @description
 * @date 17:16 2022/5/18
 */
public class DirectSender {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_direct_exchange";
        String routingKey = "test_direct_routingKey";
        String msg = "Hello world Rabbitmq for direct exchange message";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
    }
}
