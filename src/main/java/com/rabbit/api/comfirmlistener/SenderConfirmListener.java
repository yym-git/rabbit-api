package com.rabbit.api.comfirmlistener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author ym.y
 * @description
 * @date 11:14 2022/5/19
 */
@Slf4j
public class SenderConfirmListener {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setPassword("guest");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //确认消息必须设置该属性
        channel.confirmSelect();
        String exchangeName = "test_confirmListener_exchange";
        String routingKey = "confirm.save";
        String msg = "Hello world rabbitmq co" +
                "nfirm listener message......";
        //添加监听回调
        channel.addConfirmListener(new ConfirmListener(){

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                log.info("==============================消息发送成功==============");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.info("==============================消息发送失败==============");
            }
        });
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
    }
}
