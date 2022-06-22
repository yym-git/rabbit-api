package com.rabbit.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ym.y
 * @description 死信队列和消息过期
 * @date 13:34 2022/5/19
 */
@Slf4j
public class SenderDLXExchange {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchange = "test_dlx_exchange";
        String routingKey = "group.abx.dd";
        String msg = "Hello world rabbitmq for dlx message";
        Map<String, Object> header = new HashMap<>();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .headers(header)
                //设置消息过期时间
                .expiration("6000")
                .build();
        channel.basicPublish(exchange, routingKey, properties, msg.getBytes());
        channel.close();
        connection.close();
    }
}
