package com.rabbitmq.study.rabbitmq.api.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerFanout {

    static String EXCHANGE_NAME = "test.fanout.exchange";
    static String ROUTING_KEY = "";
    static String msg = "test.direst......";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();

        Channel channel = connection.createChannel();

        for (int i = 0; i < 20; i++)
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false, false, null, (msg + i).getBytes());
    }
}
