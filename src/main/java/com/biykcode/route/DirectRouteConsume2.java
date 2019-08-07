package com.biykcode.route;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  路由模式：
 *     1. 创建连接
 *     2. 创建通道
 *     3. 交换机声明
 *     4. 队列声明
 *     5. 交换机和队列指定路由键绑定
 *        可以绑定多个键，这里指定为info和error
 *     6. 回调对象创建
 *     7. 监听队列，消费消息
 *
 * @author biyukun
 * @date 2019-08-06
 */
public class DirectRouteConsume2 {

  private static final String EXCHANGE_NAME = "direct_exchange";
  private static final String QUEUE_NAME = "route_queue2";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String str = new String(message.getBody(), "utf-8");
      System.out.println("[directRoute2] consume '" + str +"'");
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
  }
}
