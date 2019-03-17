package com.rabbitmq.study.rabbitmq.api.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    private static String queueName = "test";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();

        //获取信道
        Channel channel = connection.createChannel();

        String msg = "Hello World !";

        for (int i = 0; i < 10; i++)
            //发送消息
            channel.basicPublish("", queueName, false, null, (msg + i).getBytes());

        channel.close();
        connection.close();

    }
}
