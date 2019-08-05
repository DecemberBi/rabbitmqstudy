package com.biykcode.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @description TODO
 * @date 2019-08-01
 */
public class ConnectionUtils {

  public static Connection getConnection() throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    Connection connection = connectionFactory.newConnection();
    return connection;
  }

  public static Channel getChannel() throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    Connection connection = connectionFactory.newConnection();
    Channel channel = connection.createChannel();
    return channel;
  }
}
