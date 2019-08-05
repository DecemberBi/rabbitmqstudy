package com.biykcode.work;

import com.biykcode.util.ConnectionUtils;
import com.biykcode.util.WorkUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @date 2019-08-02
 */
public class WorkConsume1 {

  private static final String QUEUE_NAME = "work_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String messageStr = new String(message.getBody(), "utf-8");
      System.out.println("[work1] consume " + messageStr);
      try {
        WorkUtil.doWork(messageStr);
      } finally {
        System.out.println("[work1] done");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    channel.close();
    connection.close();
  }


}
