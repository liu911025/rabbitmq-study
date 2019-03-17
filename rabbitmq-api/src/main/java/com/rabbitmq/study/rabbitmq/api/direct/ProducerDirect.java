package com.rabbitmq.study.rabbitmq.api.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerDirect {

    static String EXCHANGE_NAME = "test.direct.exchange";
    static String ROUTING_KEY = "test.direst";
    static String msg = "test.direst......";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //发送消息
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false, false, null, msg.getBytes());
        //关闭信道
        channel.close();
        //关闭连接
        connection.close();
    }
}
