package com.rabbit.api.comfirmlistener;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 11:15 2022/5/19
 */
@Slf4j
public class ReceiverConfirmListener {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_confirmListener_exchange";
        String queueName = "test_confirmListener_queue";
        String routingKey = "confirm.#";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true,
                false, false, null);
        channel.queueDeclare(queueName, false, false, true, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               log.info("接收到消息-:{}",new String(body));
            }
        };
        channel.basicConsume(queueName, true, "confirm消息", consumer);
    }
}
