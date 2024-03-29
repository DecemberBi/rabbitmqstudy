package com.biykcode.work;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列：
 *    1. 创建连接
 *    2. 创建通道
 *    3. 声明队列
 *    4. 发布消息
 *    5. 关闭通道和队列
 *
 * @author biyukun
 * @date 2019-08-02
 */
public class WorkSend {

  private static final String QUENE_NAME = "work_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    String message = "work hard";
    channel.queueDeclare(QUENE_NAME, false, false, false, null);
    for (int i = 0; i < 6; i++) {
      message += ".";
      channel.basicPublish("", QUENE_NAME, null, message.getBytes());
      System.out.println("[work] send '" + message + "'");
    }
    channel.close();
    connection.close();
  }
}
