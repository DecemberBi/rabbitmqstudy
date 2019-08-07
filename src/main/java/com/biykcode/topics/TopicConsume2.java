package com.biykcode.topics;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @date 2019-08-07
 */
public class TopicConsume2 {

  private static final String QUEUE_NAME = "topic_queue_2";
  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
    channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "11");
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String msg = new String(message.getBody());
      System.out.println("[topic] consume2 " + msg);
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
  }
}
