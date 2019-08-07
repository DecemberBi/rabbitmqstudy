package com.biykcode.topics;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 主题队列：
 *    和路由队列的区别是exchange指定为topic，可以使用通配符进行匹配
 *    当路由键为固定字符时，和路由模式功能相同
 *    当路由键为#时，和广播模式相同
 * @author biyukun
 * @date 2019-08-07
 */
public class TopicSend {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
    String message = "topic blue.cat";
    String routingKey = "blue.cat";
    channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
    System.out.println("[topic] send " + message);
    channel.close();
    connection.close();
  }
}
