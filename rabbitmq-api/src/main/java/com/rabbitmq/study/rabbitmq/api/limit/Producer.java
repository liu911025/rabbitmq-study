package com.rabbitmq.study.rabbitmq.api.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者消息确认机制
 */
public class Producer {

    static String EXCHANGE_NAME = "test.qos.exchange";
    static String ROUTING_KEY = "test.qos.key";
    static String MSG = "Hello, RabbitMQ send qos msg";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            for (int i = 0; i <5; i++)
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, null, MSG.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
