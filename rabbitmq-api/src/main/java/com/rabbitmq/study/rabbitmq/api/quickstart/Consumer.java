package com.rabbitmq.study.rabbitmq.api.quickstart;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static String queueName = "test";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();

        //创建信道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);

        //创建消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //设置信道
        channel.basicConsume(queueName, consumer);

        while (true) {
            //消费消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            byte[] body = delivery.getBody();
            System.out.println(new String(body));
        }
    }
}
