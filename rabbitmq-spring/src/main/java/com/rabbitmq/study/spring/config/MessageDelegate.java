package com.rabbitmq.study.spring.config;

import com.rabbitmq.study.spring.entity.Order;
import com.rabbitmq.study.spring.entity.Packaged;

import java.io.File;
import java.util.Map;

public class MessageDelegate {

    /**
     * 适配器默认反射方法名: handleMessage
     * @param body
     */
    public void handleMessage(byte[] body){
        System.err.println("MessageListenerAdapter 默认方法消息内容: " + new String(body));
    }

    public void consumerMessage(byte[] body){
        System.err.println("MessageListenerAdapter consumerMessage方法消息内容: " + new String(body));
    }

    // json对应Map
    public void consumerMessage(Map body){
        System.err.println("MessageListenerAdapter Map 方法消息内容: " + body);
    }

    public void consumeMessage(com.rabbitmq.study.spring.entity.Order order){
        System.err.println("MessageListenerAdapter Order 方法消息内容: " + order);
    }

    public void consumerMessage(Packaged packaged){
        System.err.println("MessageListenerAdapter Packaged 方法消息内容: " + packaged);
    }

    public void consumerMessageString(String body){
        System.err.println("MessageListenerAdapter consumerMessage String 方法消息内容: " + body);
    }

    public void method1(String body){
        System.err.println("MessageListenerAdapter method1 String 方法消息内容: " + body);
    }

    public void method2(String body){
        System.err.println("MessageListenerAdapter method2 String 方法消息内容: " + body);
    }
    public void consumeMessage(File file) {
        System.err.println("文件对象 方法, 消息内容:" + file.getName());
    }
}
