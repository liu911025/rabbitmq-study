package com.rabbitmq.study.rabbitmq.api.returnListener;

import com.rabbitmq.client.*;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者消息确认机制
 */
public class Producer {

    static String EXCHANGE_NAME = "test.return.exchange";
    static String ROUTING_KEY = "test.return.key";
    static String ROUTING_KEY_ERROR = "QQ.QQ";
    static String MSG = "Hello, RabbitMQ send return msg";

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionUtil.getConnection();

            Channel channel = connection.createChannel();

            /*
                参数:
                    1.交换机
                    2.路由键
                    3.true:监听器会接收到路由不可达的消息然后进行后续处理
                      false:broker端自动删除该消息,  returnListener监听不到
                    4.自定义参数
                    5.消息内容
             */
            //channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, null, MSG.getBytes());
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_ERROR, true, null, MSG.getBytes());

            channel.addReturnListener(new ReturnListener() {
                @Override
                public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("replyCode : " + replyCode);
                    System.out.println("replyText : " + replyText);
                    System.out.println("exchange : " + exchange);
                    System.out.println("routingKey : " + routingKey);
                    System.out.println("properties : " + properties);
                    System.out.println("body : " + new String(body));
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
