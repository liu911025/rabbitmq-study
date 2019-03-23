package com.rabbitmq.study.rabbitmq.api.limit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费端限流
 */
public class Consumer {

    static String EXCHANGE_NAME = "test.qos.exchange";
    static String QUEUE_NAME = "test.queue.qos";
    static String ROUTING_KEY = "test.qos.#";
    static String TYPE = "topic";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, TYPE, true);
            channel.queueDeclare(QUEUE_NAME, true, false, true, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            /*
                参数:
                    1.设置消息大小
                    2.一次接受多少条
                    3.设置channel为channel级别还是consumer级别
                    注意:需要手动ack
             */
            channel.basicQos(0, 1, false);

            //限流需要将自动ack改为false,否则无效
            channel.basicConsume(QUEUE_NAME, false, new CustomConsumer(channel));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
