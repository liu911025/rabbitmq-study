package com.rabbitmq.study.spring.config;


import com.rabbitmq.client.Channel;
import com.rabbitmq.study.spring.convert.ImageMessageConverter;
import com.rabbitmq.study.spring.convert.PDFMessageConverter;
import com.rabbitmq.study.spring.entity.Order;
import com.rabbitmq.study.spring.entity.Packaged;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@ComponentScan("com.rabbitmq.study.spring.*")
public class RabbitMQConfig {

    @Autowired
    QueueConfig queueConfig;

    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses("192.168.25.135:5672");
        factory.setUsername("root");
        factory.setPassword("root");
        factory.setVirtualHost("/");
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);//设置为true,spring容器加载
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }


    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueues(queueConfig.queue001(), queueConfig.queue002(), queueConfig.queue003(), queueConfig.queue_image(), queueConfig.queue_pdf());//添加队列
        container.setConcurrentConsumers(1);    //消费者数量
        container.setMaxConcurrentConsumers(5); //最大消费者

        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //创建标志
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return  s + "_" + UUID.randomUUID();
            }
        });

        /*
        //一.消息监听
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                System.err.println("MessageListener: " + new String(message.getBody()));
            }
        });
        */

        /*
        //二.消息适配
        //默认是有自己的方法名字的：handleMessage
    	// 可以自己指定一个方法的名字: consumeMessage
    	// 也可以添加一个转换器: 从字节数组转换为String
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumerMessage");    //修改默认方法名
        adapter.setDefaultListenerMethod("consumerMessageString");    //修改默认方法名
        //MessageListenerAdapter默认方法参数为byte,需要将类型转换
        adapter.setMessageConverter(new TextMessageConverter());
        */

        /*
        //三.根据队列名反射对应的方法
        //队列名称 和 方法名称 也可以进行一一的匹配
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        Map<String, String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put(queueConfig.queue001().getName(), "method1");
        queueOrTagToMethodName.put(queueConfig.queue002().getName(), "method2");
        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        //MessageListenerAdapter默认方法参数为byte,需要将类型转换
        adapter.setMessageConverter(new TextMessageConverter());
        */

        /*
        //支持json格式转换器
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumerMessage");
        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        jsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(jsonMessageConverter);
        container.setMessageListener(adapter);
        */

        // DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();

        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("order", Order.class);
        idClassMapping.put("packaged", Packaged.class);

        javaTypeMapper.setIdClassMapping(idClassMapping);

        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        adapter.setMessageConverter(jackson2JsonMessageConverter);
        container.setMessageListener(adapter);

        //1.4 ext convert

        /*MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);

        adapter.setMessageConverter(convert);
        container.setMessageListener(adapter);*/
        return container;
    }
}
