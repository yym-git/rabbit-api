package com.rabbit.api.directexchange;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 17:16 2022/5/18
 */
@Slf4j
public class DirectReceiver {
    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setNetworkRecoveryInterval(3000);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_direct_exchange";
        String routingKey = "test_direct_routingKey";
        String queueName = "test_direct_queue";
        //direct模式的exchange，路由规则必须完全匹配，同时必须声exchange
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定queue、exchange、RoutingKey三者之间的关系
        channel.queueBind(queueName, exchangeName, routingKey);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到消息----------------");
                log.info("consumerTag:{}", consumerTag);
                log.info("envelope:{}", envelope);
                log.info("properties:{}", properties);
                log.info("body:{}", new String(body));
            }
        };

        channel.basicConsume(queueName, true, "direct消息消费", consumer);
    }
}
