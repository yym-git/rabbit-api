package com.rabbit.api.defaultexchange;


import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 15:49 2022/5/18
 */
@Slf4j
public class Receiver {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "test001";
        /**
         * 接收端也不需要进行bind
         */
//        channel.queueBind()
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到的消息--------------------------");
                log.info("consumerTag==:{}", consumerTag);
                log.info("envelope==:{}", envelope);
                log.info("BasicProperties==:{}", properties);
                log.info("body==:{}", new String(body));
                log.info("接收到的消息--------------------------");
            }
        };
        channel.basicConsume(queueName, true, "消费者标签", defaultConsumer);
    }
}
