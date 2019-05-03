package com.example.demo.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitReceive {

    @RabbitListener(bindings = @QueueBinding(
            value =  @Queue(value = "springboot.abc", durable = "true", ignoreDeclarationExceptions = "true"),
            exchange = @Exchange(value = "exchange-1", durable = "true", ignoreDeclarationExceptions = "true", type = "topic"),
            key = "springboot.#"
        )
    )
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws IOException {
        System.out.println("--------------------start--------------------------");
        System.out.println("消息体: " + message.getPayload());
        Long tag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(tag, false);
        System.out.println("--------------------start--------------------------");

    }

}
