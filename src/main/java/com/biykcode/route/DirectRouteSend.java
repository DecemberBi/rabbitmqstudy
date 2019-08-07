package com.biykcode.route;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式：
 *    1. 创建连接
 *    2. 创建通道
 *    3. 声明交换机为direct
 *    4. 向指定路由键的交换机发送消息
 *
 *    路由键：在生产者中设定的路由键，在消费者中队列绑定交换机的时候使用，
 *          只有生产者的路由键和绑定交换机和队列的键相同时，交换机才会发送消息到队列
 * @author biyukun
 * @date 2019-08-06
 */
public class DirectRouteSend {

  public static final String EXCHANGE_NAME = "direct_exchange";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    String message = "direct route";
    String routingKey = "error";
    channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
    System.out.println("[directRoute] send '" + message + "'");
    channel.close();
    connection.close();
  }

}
