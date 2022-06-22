package com.rabbit.api.fanoutexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ym.y
 * @description fanout交换机存在多个消费者消费同一个queue时，使用的轮询的方式进行消费
 * @date 18:15 2022/5/18
 */
public class FanoutSender {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName="test_fanout_exchange";
        for(int i =1; i<=5; i++){
            String msg ="Hello world Rabbitmq for fanout message_"+i;
            channel.basicPublish(exchangeName, "", null, msg.getBytes());
        }

    }
}
