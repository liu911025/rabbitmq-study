package com.rabbitmq.study.rabbitmq.api.limit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

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
        System.out.println("------------------------myConsumer message start----------------------------");
        /*
            参数:
                1.消息标志
                2.是否批量ack
         */
        channel.basicAck(envelope.getDeliveryTag(), false);
        System.out.println("------------------------myConsumer message end------------------------------");
    }
}
