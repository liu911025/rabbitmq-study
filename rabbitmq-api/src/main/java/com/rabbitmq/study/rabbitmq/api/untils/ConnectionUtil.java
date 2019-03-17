package com.rabbitmq.study.rabbitmq.api.untils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {

    private static String HOST = "192.168.25.135";

    private static int PORT = 5672;

    private static String VHOST = "/";

    private static String USERNAME = "root";

    private static String PASSWORD = "root";

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);
        return factory;
    }

    public static Connection getConnection() throws IOException, TimeoutException {
        return getConnectionFactory().newConnection();
    }

}
