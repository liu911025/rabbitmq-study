package com.rabbitmq.study.rabbitmq.api.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者消息确认机制
 */
public class Producer {

    static String EXCHANGE_NAME = "test.dlx.exchange";
    static String QUEUE_NAME = "test.dlx.queue";
    static String ROUTING_KEY = "test.del.save";
    static String TYPE = "topic";
    static String MSG = "Hello, RabbitMQ send qos msg";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            for (int i = 0; i <5; i++){
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                        .deliveryMode(2)
                        .contentType("UTF-8")
                        .expiration("10000")    //设置消息过期时间 10s
                        .build();
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, properties, MSG.getBytes());
            }


            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Map<String, Object> headers = properties.getHeaders();
                    int num = (int) headers.get("num");
                    System.out.println("producer return listener num is " + num);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
