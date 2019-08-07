package com.biykcode.work;

import com.biykcode.util.ConnectionUtils;
import com.biykcode.util.WorkUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列：
 *    consume1: sleep 1s
 *    consume2: sleep 2s
 *
 *    1. work1   轮询分发
 *    2. work2   公平分发
 *        2.1 手动反馈  work2有详细说明
 *        2.2 设置持久性
 *              需要在队列声明的时候设置durable为true，生产者和消费者都需要设置
 *              生产者需要在发布消息的时候设置第三个属性值为MessageProperties.PERSISTENT_TEXT_PLAIN
 *
 *
 * @author biyukun
 * @date 2019-08-02
 */
public class WorkConsume1 {

  private static final String QUEUE_NAME = "work_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
//    work1();
    work2();
  }

  private static void work1() throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String messageStr = new String(message.getBody(), "utf-8");
      System.out.println("[work1] consume " + messageStr);
      try {
        WorkUtil.doWork1(messageStr);
      } finally {
        System.out.println("[work1] done");
      }
    };
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
  }

  private static void work2() throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    // 公平分发，设置每次分发的最大值
    channel.basicQos(1);
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      String messageStr = new String(message.getBody(), "utf-8");
      System.out.println("[work1] consume " + messageStr);
      try {
        WorkUtil.doWork1(messageStr);
      } finally {
        System.out.println("[work1] done");
        // 手动设置反馈
        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
      }
    };
    // 手动反馈，设置自动反馈为false
    boolean autoAck = false;
    channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> {
    });
  }


}
