package com.rabbitmq.study.rabbitmq.api.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerDirect {

    static String EXCHANGE_NAME = "test.direct.exchange";
    static String ROUTING_KEY = "test.direst";
    static String TYPE = "direct";
    static String QUEUE_NAME = "test.direct.queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建信道
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, TYPE);
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //绑定交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY, null);
        //定义消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //设置信道
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            System.out.println(new String(consumer.nextDelivery().getBody()));
        }
    }

}
