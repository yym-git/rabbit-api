package com.rabbit.api.fanoutexchange;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 18:15 2022/5/18
 */
@Slf4j
public class FanoutReceiver {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setNetworkRecoveryInterval(3000);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName="test_fanout_queue";
        String exchangeName="test_fanout_exchange";
        //对于fanout模式的exchange，指定RoutingKey没有任何意义
        String routingKey="1111";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT,
                true, true, false, null);
        channel.queueDeclare(queueName, false, false, true, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到的消息：{}",new String(body));
            }
        };
        channel.basicConsume(queueName, true, "fanout消息",consumer);
    }
}
