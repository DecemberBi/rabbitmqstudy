package com.biykcode.hello;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列：
 *    1. 创建连接
 *    2. 创建通道
 *    3. 声明队列
 *    4. 发布消息
 *    5. 关闭通道和连接
 *
 * @author biyukun
 * @date 2019-08-01
 */
public class HelloSend {

  private static final String QUEUE_NAME = "hello_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    String message = "Hello Queue";
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    System.out.println("[hello] send '" + message + "'");
    channel.close();
    connection.close();
  }
}
