package com.rabbit.api.requeue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ym.y
 * @description
 * @date 12:06 2022/5/19
 */
@Slf4j
public class Sender {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "test001";
        channel.queueDeclare(queueName, false, false, false, null);
        for (int i = 0; i <= 5; i++) {
            String msg = "Hello world msg " + i;
            Map<String, Object> headers = new HashMap<>();
            headers.put("flag", i);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .headers(headers)
                    .build();
            channel.basicPublish("", queueName, properties, msg.getBytes());
        }
    }
}
