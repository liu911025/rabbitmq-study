package com.rabbitmq.study.rabbitmq.api.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者消息确认机制
 */
public class Producer {

    static String EXCHANGE_NAME = "test.confirm.exchange";
    static String QUEUE_NAME = "test.queue.confirm";
    static String ROUTING_KEY = "test.confirm.key";
    static String MSG = "Hello, RabbitMQ send msg";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            //开启消息确认模式
            // 指定消息投递模式: 消息确认模式
            channel.confirmSelect();

            /*
                参数:
                    1.交换机
                    2.路由键
                    3.true:监听器会接收到路由不可达的消息然后进行后续处理
                      false:broker端自动删除该消息
                    4.自定义参数
                    5.消息内容
             */
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, null, MSG.getBytes());

            //添加消息确认监听
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("MSG is ack !");
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("MSG is nack !");
                }
            });

           // Thread.sleep(5000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
