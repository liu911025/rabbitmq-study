package com.rabbitmq.study.spring.config;


import com.rabbitmq.client.Channel;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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

        /*//一.消息监听
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                System.err.println("MessageListener: " + new String(message.getBody()));
            }
        });*/

        //二.消息适配
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumerMessage");    //修改默认方法名
        adapter.setDefaultListenerMethod("consumerMessageString");    //修改默认方法名

        //MessageListenerAdapter默认方法参数为byte,需要将类型转换
        adapter.setMessageConverter(new TextMessageConverter());

        container.setMessageListener(adapter);
        return container;
    }
}
