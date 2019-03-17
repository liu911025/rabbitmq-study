package com.rabbitmq.study.rabbitmq.api.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.study.rabbitmq.api.untils.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ProducerMessage {

    private static String queueName = "test";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();

        //获取信道
        Channel channel = connection.createChannel();

        String msg = "Hello World !";
        Map<String, Object> headers = new HashMap<>();
        headers.put("a", "a");
        headers.put("b", "b");

       AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .appId("qqq")
                .deliveryMode(2)
                .contentEncoding("UTF-8")
                .contentType("666===>>>")
                .expiration("10000")    //过期时间
                .headers(headers)       //自定义参数
                .build();
      /*  AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
        AMQP.BasicProperties properties = builder.expiration("10000").build();*/

        for (int i = 0; i < 10; i++){
            //发送消息
            //channel.basicPublish("", queueName, false, properties, (msg + i).getBytes());
            channel.basicPublish("", queueName, properties, (msg + i).getBytes());
        }



        channel.close();
        connection.close();

    }
}
