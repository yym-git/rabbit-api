package com.rabbit.api.limit;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 12:06 2022/5/19
 */
@Slf4j
public class Receiver {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setNetworkRecoveryInterval(3000);
        connectionFactory.setAutomaticRecoveryEnabled(true);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        /**
         *   RabbitMQ 提供了一种 qos （服务质量保证）功能，
         *   即在非自动确认消息的前提下，如果一定数目的消息（通过基于 consume 或者 channel 设置 Qos 的值）未被确认前，不进行消费新的消息。
         *   prefetchSize：消息大小限制，一般设置为0，不做限制
         *   prefetchCount: 不要同时给同一个消费者推送多余prefetchCount的消息，一旦多余prefetchCount数量的消息没有ack，则阻塞
         *   global: 用于限制将以上配置是否运用于channel级别还是consumer级别
         *   channel.basicQos();
         */
        channel.basicQos(1);
        String queueName = "test001";
        channel.queueDeclare(queueName, false, false, false, null);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("接收到消息：{}", new String(body));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if ((Integer) properties.getHeaders().get("flag") >= 3) {
                    //消费失败
                    //参数说明：消息标识，是否批量应答，是否重新投递消息
                    //生产上重回队列参数一般都是设置为false
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
                } else {
                    //消费成功
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(queueName, false, "重回消息", consumer);
    }
}
