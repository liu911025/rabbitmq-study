package com.rabbitmq.study.spring.config;

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

    public void consumerMessageString(String body){
        System.err.println("MessageListenerAdapter consumerMessage String 方法消息内容: " + body);
    }

    public void method1(String body){
        System.err.println("MessageListenerAdapter method1 String 方法消息内容: " + body);
    }

    public void method2(String body){
        System.err.println("MessageListenerAdapter method2 String 方法消息内容: " + body);
    }
}
