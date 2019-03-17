package com.rabbitmq.study.rabbitmq.api.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerTopic {

    static String EXCHANGE_NAME = "test.topic.exchange";
    static String ROUTING_KEY_1 = "test.topicA";
    static String ROUTING_KEY_2 = "test.topicB";
    static String ROUTING_KEY_3 = "test.topicC.ccc";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_1, false, null, ROUTING_KEY_1.getBytes());
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_2, false, null, ROUTING_KEY_2.getBytes());
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_3, false, null, ROUTING_KEY_3.getBytes());

        channel.close();
        connection.close();
    }
}
