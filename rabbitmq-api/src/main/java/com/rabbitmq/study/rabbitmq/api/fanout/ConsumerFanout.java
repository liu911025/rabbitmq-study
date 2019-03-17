package com.rabbitmq.study.rabbitmq.api.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerFanout {

    static String EXCHANGE_NAME = "test.fanout.exchange";
    static String QUEUE_NAME = "test.fanout.queue";
    static String TYPE = "fanout";
    static String ROUTING_KEY = "";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, TYPE, false);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, null);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            System.out.println(new String(consumer.nextDelivery().getBody()));
        }
    }
}
