package com.rabbit.api.dlx;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ym.y
 * @description
 * @date 13:34 2022/5/19
 */
@Slf4j
public class ReceiverDLXExchange {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        String queueName = "test_dlx_queue";
        String exchangeName = "test_dlx_exchange";
        String routingKey = "group.*";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, false, false
                , false, null);
        Map<String, Object> arguments = new HashMap<>();
        //正常对队列中添加参数x-dead-letter-exchange,值为死信队列
        arguments.put("x-dead-letter-exchange", "dlx.exchange");
        channel.queueDeclare(queueName, false, false, false, arguments);
        channel.queueBind(queueName, exchangeName, routingKey);

        //声明死信队列
        channel.exchangeDeclare("dlx.exchange", BuiltinExchangeType.TOPIC, false, false, null);
        channel.queueDeclare("dlx.queue", false, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到的消息:{}", new String(body));
                log.info("envelope:{}", envelope);
                log.info("properties:{}", properties);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
