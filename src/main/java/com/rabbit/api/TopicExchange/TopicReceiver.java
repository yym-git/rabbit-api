package com.rabbit.api.TopicExchange;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 17:46 2022/5/18
 */
@Slf4j
public class TopicReceiver {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPort(5672);
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPassword("guest");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_topic_exchange";
        String queueName = "test_topic__queue";
        //匹配一个或多个字符
        String routingKey = "user.#";

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true, true, false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到的消息...............");
                log.info("consumerTag:{}", consumerTag);
                log.info("envelope:{}", envelope);
                log.info("properties:{}", properties);
                log.info("body:{}", new String(body));
            }
        };
        channel.basicConsume(queueName, true, "topic消息", consumer);
    }
}
