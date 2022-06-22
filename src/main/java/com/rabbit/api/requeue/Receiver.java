package com.rabbit.api.requeue;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.omg.PortableServer.THREAD_POLICY_ID;

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
                if ((Integer) properties.getHeaders().get("flag") == 0) {
                    //消费失败
                    //参数说明：消息标识，是否批量应答，是否重新投递消息
                    //生产上重回队列参数一般都是设置为false
                    channel.basicNack(envelope.getDeliveryTag(),false,true);
                }else{
                    //消费成功
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        channel.basicConsume(queueName, false, "重回消息", consumer);
    }
}
