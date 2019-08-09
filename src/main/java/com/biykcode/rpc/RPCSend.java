package com.biykcode.rpc;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @date 2019-08-09
 */
public class RPCSend {

  private static final String RPC_REQUEST_QUEUE = "rpc_request_queue";
  private static final String RPC_REPLY_QUEUE = "rpc_reply_queue";

  public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
    String uuid = UUID.randomUUID().toString();
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    AMQP.BasicProperties basicProperties = new AMQP.BasicProperties()
      .builder()
      .correlationId(uuid)
      .replyTo(RPC_REPLY_QUEUE).build();
    channel.queueDeclare(RPC_REQUEST_QUEUE, false, false, false, null);
    channel.queueDeclare(RPC_REPLY_QUEUE, false, false, false, null);
    String message = "1";
    channel.basicPublish("", RPC_REQUEST_QUEUE, basicProperties, message.getBytes());
    System.out.println("[rpc] client send '" + message + "'");
    final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

    String ctag = channel.basicConsume(RPC_REPLY_QUEUE, true, (consumerTag, delivery) -> {
      if (delivery.getProperties().getCorrelationId().equals(uuid)) {
        response.offer(new String(delivery.getBody(), "UTF-8"));
      }
    }, consumerTag -> { });

    String result = response.take();
    channel.basicCancel(ctag);
    System.out.println("[rpc] client callback result '" +result +"'");
    channel.close();
    connection.close();
  }
}
