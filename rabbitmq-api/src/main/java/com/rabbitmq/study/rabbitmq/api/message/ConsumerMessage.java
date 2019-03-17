package com.rabbitmq.study.rabbitmq.api.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ConsumerMessage {

    private static String queueName = "test";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();

        //创建信道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);

        //创建消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //设置信道
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            //消费消息
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            System.out.println(new String(delivery.getBody()));

            AMQP.BasicProperties properties = delivery.getProperties();
            if (null != properties) {
                System.out.println("appId: " + properties.getAppId());
                System.out.println("ContentEncoding: " + properties.getContentEncoding());
                System.out.println("ContentType: " + properties.getContentType());

                Map<String, Object> headers = properties.getHeaders();
                if (null != headers) {
                    System.out.println("---------------------headers-----------------------");
                    for (String key : headers.keySet()) {
                        System.out.println(key + ">>>>>>>>" +headers.get(key));
                    }
                    System.out.println("---------------------headers-----------------------");
                }
            }
        }
    }
}
