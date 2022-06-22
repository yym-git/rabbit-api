package com.rabbit.api.defaultexchange;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author ym.y
 * @description
 * @date 15:49 2022/5/18
 */
public class Sender {
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPassword("guest");

        //创建Connection
        Connection connection = connectionFactory.newConnection();
        //创建channel
        Channel channel = connection.createChannel();
        String queueName = "test001";
        channel.queueDeclare(queueName, false, false, false, null);
        //消息头
        Map<String, Object> headers = new HashMap<>();
        //消息体
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .deliveryMode(2) //持久化消息
                .headers(headers)
                .contentEncoding("UTF-8")
                .build();
        for (int i = 0; i < 5; i++) {
            String msg = "Hello world rabbit " + i;
            /**
             * 发送消息时不指定exchange则会走默认的AMQP Default交换机，根据queueName去路由
             */
            channel.basicPublish("", queueName, props, msg.getBytes());
        }


    }
}
