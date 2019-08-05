package com.biykcode.hello;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列：
 *     1. 创建连接
 *     2. 创建通道
 *     3. 声明队列
 *     4. 创建回调对象
 *     5. 消费消息，通过回调方法处理收到的消息
 *
 * @author biyukun
 * @date 2019-08-01
 */
public class HelloConsume {

  private static final String QUEUE_NAME = "hello_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println("[hello] consume '" + message + "'");
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  }
}
