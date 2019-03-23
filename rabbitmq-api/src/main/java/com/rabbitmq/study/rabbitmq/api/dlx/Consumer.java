package com.rabbitmq.study.rabbitmq.api.dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列设置
 */
public class Consumer {

    static String EXCHANGE_NAME = "test.dlx.exchange";
    static String QUEUE_NAME = "test.dlx.queue";
    static String ROUTING_KEY = "test.del.save";
    static String TYPE = "topic";

    static String DLX_EXCHANGE_NAME = "dlx.exchange";
    static String DLX_QUEUE_NAME = "dlx.queue";
    static String DLX_ROUTING_KEY = "test.del.#";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            /*
               声明交换机和队列
             */
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
            channel.exchangeDeclare(EXCHANGE_NAME, TYPE, true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            /*
               声死信队列, 其实与普通队列一样
             */
            channel.exchangeDeclare(DLX_EXCHANGE_NAME, TYPE, true);
            channel.queueDeclare(DLX_QUEUE_NAME, true, false, true, null);
            channel.queueBind(DLX_QUEUE_NAME, DLX_EXCHANGE_NAME, DLX_ROUTING_KEY);

            //开启限流
            channel.basicQos(0, 1, false);
            //限流需要将自动ack改为false,否则无效
            channel.basicConsume(QUEUE_NAME, false, new CustomConsumer(channel));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
