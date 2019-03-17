package com.rabbitmq.study.rabbitmq.api.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerTopic {


    static String EXCHANGE_NAME = "test.topic.exchange";
    static String QUEUE_NAME = "test.topic.exchange";
    static String TYPE = "topic";

    /*
        test.*  *只匹配一个字符,如: test.a; test.b;
        test.#  #匹配一个或多个字符,如: test.a; test.b; test.a.b
     */
    //static String ROUTING_KEY = "test.#";
    static String ROUTING_KEY = "test.*";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, TYPE, false);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            System.out.println(new String(consumer.nextDelivery().getBody()));
        }
    }


}
