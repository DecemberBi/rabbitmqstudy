package com.biykcode.work;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @description TODO
 * @date 2019-08-02
 */
public class WorkSend {

  private static final String QUENE_NAME = "work_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    String message = "work hard";
    channel.queueDeclare(QUENE_NAME, false, false, false, null);
    channel.basicPublish("", QUENE_NAME, null, message.getBytes());
    System.out.println("[x] send " + message);
    channel.close();
    connection.close();
  }
}
