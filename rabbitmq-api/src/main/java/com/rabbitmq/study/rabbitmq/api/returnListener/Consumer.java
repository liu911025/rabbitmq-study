package com.rabbitmq.study.rabbitmq.api.returnListener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    static String EXCHANGE_NAME = "test.return.exchange";
    static String QUEUE_NAME = "test.queue.return";
    static String ROUTING_KEY = "test.return.*";
    static String MSG = "Hello, RabbitMQ send return msg";
    static String TYPE = "topic";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, TYPE, true);
            channel.queueDeclare(QUEUE_NAME, true, false, true, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(QUEUE_NAME, true, consumer);

            for(;;) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                System.out.println(new String(delivery.getBody(), "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
