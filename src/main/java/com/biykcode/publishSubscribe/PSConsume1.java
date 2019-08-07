package com.biykcode.publishSubscribe;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模型：
 *    1. 创建连接
 *    2. 创建通道
 *    3. 交换机声明
 *    4. 获取绑定的所有队列
 *    5. 交换机和队列绑定
 *    6. 定义回调对象
 *    7. 消费消息
 *
 * @author biyukun
 * @date 2019-08-06
 */
public class PSConsume1 {

  public static final String EXCHANGE_NAME = "fanout";


  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    System.out.println(queueName);
    channel.queueBind(queueName, EXCHANGE_NAME, "");
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String str = new String(message.getBody(), "utf-8");
      System.out.println("[ps] consume '" + str + "'");
    };
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
  }
}
