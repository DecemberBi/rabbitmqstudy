package com.biykcode.work;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @date 2019-08-02
 */
public class WorkConsume {

  private static final String QUEUE_NAME = "work_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String messageStr = new String(message.getBody(), "utf-8");
      System.out.println("[work1] consume " + messageStr);
      try {
        doWork(messageStr);
      } catch (InterruptedException e) {
        System.out.println("error...");
      } finally {
        System.out.println("[work1] done");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    channel.close();
    connection.close();
  }

  public static void doWork(String task) throws InterruptedException {
    for (char ch : task.toCharArray()) {
      if (ch == '.') {
        Thread.sleep(1000);
      }
    }
  }
}
