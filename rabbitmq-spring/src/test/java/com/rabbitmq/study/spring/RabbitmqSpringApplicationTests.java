package com.rabbitmq.study.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.study.spring.entity.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqSpringApplicationTests {

    @Test
    public void contextLoads() {
    }


    @Autowired
    private RabbitAdmin rabbitAdmin;

    private static String DIRECT_EXCHANGE = "test.direct";
    private static String TOPIC_EXCHANGE = "test.topic";
    private static String FANOUT_EXCHANGE = "test.fanout";

    private static String DIRECT_QUEUE = "test.direct.queue";
    private static String TOPIC_QUEUE = "test.topic.queue";
    private static String FANOUT_QUEUE = "test.fanout.queue";

    private static String DIRECT_ROUTING_KEY= "direct";
    private static String TOPIC_ROUTING_KEY= "topic";
    private static String FANOUT_ROUTING_KEY= "fanout";


    @Test
    public void rabbitAdminTest() {
        rabbitAdmin.declareExchange(new DirectExchange(DIRECT_EXCHANGE, true, false, null));

        rabbitAdmin.declareExchange(new TopicExchange(TOPIC_EXCHANGE, true, false, null));

        rabbitAdmin.declareExchange(new FanoutExchange(FANOUT_EXCHANGE, true, false, null));

        rabbitAdmin.declareQueue(new Queue(DIRECT_QUEUE, false));

        rabbitAdmin.declareQueue(new Queue(TOPIC_QUEUE, false));

        rabbitAdmin.declareQueue(new Queue(FANOUT_QUEUE, false));

        rabbitAdmin.declareBinding(new Binding(DIRECT_QUEUE,
                Binding.DestinationType.QUEUE,
                DIRECT_EXCHANGE, DIRECT_ROUTING_KEY, new HashMap<>()));

        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue(TOPIC_QUEUE, false))		//直接创建队列
                        .to(new TopicExchange(TOPIC_EXCHANGE, false, false))	//直接创建交换机 建立关联关系
                        .with(TOPIC_ROUTING_KEY));	//指定路由Key


        rabbitAdmin.declareBinding(
                BindingBuilder
                        .bind(new Queue(FANOUT_QUEUE, false))
                        .to(new FanoutExchange(FANOUT_EXCHANGE, false, false)));

        //清空队列数据
        rabbitAdmin.purgeQueue(TOPIC_QUEUE, false);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void rabbitTemplateSendMessage() {
        MessageProperties messageProperties = new MessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();
        headers.put("desc", "aisinilehaha");
        headers.put("type", "hehe");

        String msg = "jsghkjsdfhkfsjdh kjhf sdkjhfd jkh";
        Message message = new Message(msg.getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("-------消息添加额外信息------");
                Map<String, Object> map = message.getMessageProperties().getHeaders();
                map.put("attr", "多余的");
                return message;
            }
        });
    }

    @Test
    public void rabbitTemplateSendMessage2() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        String msg = "mq消息1234";
        Message message = new Message(msg.getBytes(), messageProperties);
        rabbitTemplate.send("topic001", "spring.q", message);

        rabbitTemplate.convertAndSend("topic001", "spring.amqp", "hello object message send topic001!");
        rabbitTemplate.convertAndSend("topic002", "rabbit.abc", "hello object message send topic002! ");
    }

    @Test
    public void rabbitTemplateSendMessage3() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        String msg = "mq消息1234";
        Message message = new Message(msg.getBytes(), messageProperties);
        //rabbitTemplate.send("topic001", "spring.q", message);
        rabbitTemplate.convertAndSend("topic001", "spring.q", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("-----------------------添加额外设置------------------------------");
                MessageProperties properties = message.getMessageProperties();
                properties.getHeaders().put("dec", "aisilehaha");
                properties.getHeaders().put("attr", "add attr");
                return message;
            }
        });
    }


    @Test
    public void rabbitTemplateSendMessage4() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/plain");
        String msg = "mq消息1234";
        Message message = new Message(msg.getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topic001", "spring.q", message);

        rabbitTemplate.convertAndSend("topic002", "rabbit.q", message);
    }

    @Test
    public void testSendJsonMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("消息订单");
        order.setContent("描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }

    @Test
    public void testSendJavaMessage() throws Exception {

        Order order = new Order();
        order.setId("001");
        order.setName("订单消息");
        order.setContent("订单描述信息");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(order);
        System.err.println("order 4 json: " + json);

        MessageProperties messageProperties = new MessageProperties();
        //这里注意一定要修改contentType为 application/json
        messageProperties.setContentType("application/json");
        messageProperties.getHeaders().put("__TypeId__", "com.rabbitmq.study.spring.entity.Order");
        Message message = new Message(json.getBytes(), messageProperties);

        rabbitTemplate.send("topic001", "spring.order", message);
    }

    @Test
    public void testSendExtConverterMessage() throws Exception {
//			byte[] body = Files.readAllBytes(Paths.get("d:/002_books", "picture.png"));
//			MessageProperties messageProperties = new MessageProperties();
//			messageProperties.setContentType("image/png");
//			messageProperties.getHeaders().put("extName", "png");
//			Message message = new Message(body, messageProperties);
//			rabbitTemplate.send("", "image_queue", message);

        byte[] body = Files.readAllBytes(Paths.get("d:\\", "mysql.pdf"));
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/pdf");
        Message message = new Message(body, messageProperties);
        rabbitTemplate.send("", "pdf_queue", message);
    }
}
