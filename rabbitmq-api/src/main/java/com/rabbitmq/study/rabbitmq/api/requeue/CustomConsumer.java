package com.rabbitmq.study.rabbitmq.api.requeue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.Map;

/**
 * 自定义消费端
 */
public class CustomConsumer extends DefaultConsumer {

    private Channel channel;

    public CustomConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag,
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        Map<String, Object> headers = properties.getHeaders();
        int num = (int) headers.get("num");
        System.out.println("num: " + num);
        if (num == 3) {
            //消息重回队列
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        }else {
            channel.basicAck(envelope.getDeliveryTag(), false);
        }
    }
}
