package com.biykcode.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 创建连接：
 *    1. 创建连接工厂
 *    2. 设置连接地址
 *    3. 创建连接对象
 *
 * @author biyukun
 * @date 2019-08-01
 */
public class ConnectionUtils {

  public static Connection getConnection() throws IOException, TimeoutException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    Connection connection = connectionFactory.newConnection();
    return connection;
  }
}
