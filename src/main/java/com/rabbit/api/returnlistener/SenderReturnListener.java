package com.rabbit.api.returnlistener;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author ym.y
 * @description
 * @date 11:37 2022/5/19
 */
@Slf4j
public class SenderReturnListener {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "test_return_listener_exchange";
        String routingKey = "abcd.save";
        String routingKey2 = "return.save";
        String routingKe3y = "return.delete.abc";
        String msg = "Hello World Rabbitmq for return message.....";
        //添加监听
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText,
                                     String exchange,
                                     String routingKey,
                                     AMQP.BasicProperties properties, byte[] body) throws IOException {
                log.info("===replyCode==:{}", replyCode);
                log.info("===replyText==:{}", replyText);
                log.info("===exchange==:{}", exchange);
                log.info("===routingKey==:{}", routingKey);
                log.info("===properties==:{}", properties);
                log.info("===body==:{}", new String(body));
            }
        });
        //return消息，必须设置该属性值为true,设置为false则不会进行回调监听
        boolean mandatory = true;
        channel.basicPublish(exchangeName, routingKey, mandatory, null, msg.getBytes());
//        channel.basicPublish(exchangeName, routingKey2, mandatory, null, msg.getBytes());
    }
}
