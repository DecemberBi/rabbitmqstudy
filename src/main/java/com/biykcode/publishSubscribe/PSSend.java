package com.biykcode.publishSubscribe;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模型：
 *    1. 创建连接
 *    2. 创建通道
 *    3. 交换机声明
 *    4. 发布消息
 *    5. 关闭通道和连接
 *
 * @author biyukun
 * @date 2019-08-06
 */
public class PSSend {

  public static final String QUEUE_NAME = "ps queue";
  public static final String EXCHANGE_NAME = "fanout";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare("logs", "fanout");
    String message = "ps: do a good man";
    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
    System.out.println("[ps] send '" + message + "'");
    channel.close();
    connection.close();
  }
}
