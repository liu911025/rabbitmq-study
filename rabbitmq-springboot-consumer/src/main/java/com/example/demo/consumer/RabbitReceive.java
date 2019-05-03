package com.example.demo.consumer;

import com.rabbitmq.client.Channel;
import com.study.rabbitmq.pojo.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

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
        System.out.println("onMessage 消息体: " + message.getPayload());
        Long tag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(tag, false);
        System.out.println("--------------------end--------------------------");

    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "${spring.rabbitmq.listener.order.queue.durable}", ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.queue.ignoreDeclarationExceptions}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}", durable = "${spring.rabbitmq.listener.order.exchange.durable}", type = "${spring.rabbitmq.listener.order.exchange.type}", ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
            key = "${spring.rabbitmq.listener.order.key}"
    ))
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        System.out.println("--------------------start--------------------------");
        System.out.println("onOrderMessage 消息体: " + order.toString());
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(tag, false);
        System.out.println("--------------------end--------------------------");

    }

}
