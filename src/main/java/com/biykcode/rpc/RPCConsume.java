package com.biykcode.rpc;

import com.biykcode.util.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author biyukun
 * @date 2019-08-09
 */
public class RPCConsume {

  private static final String RPC_REQUEST_QUEUE = "rpc_request_queue";

  public static void main(String[] args) throws IOException, TimeoutException {
    Connection connection = ConnectionUtils.getConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(RPC_REQUEST_QUEUE, false, false, false, null);
    channel.queuePurge(RPC_REQUEST_QUEUE);
    channel.basicQos(1);

    Object monitor = new Object();
    DeliverCallback deliverCallback = (consumerTag, message) -> {
      AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
        .correlationId(message.getProperties().getCorrelationId()).build();
      String response = "";

      String msg = new String(message.getBody());
      System.out.println("[rpc] server consume '" +msg+ "'");
      response = work(msg);
      channel.basicPublish("", message.getProperties().getReplyTo(), basicProperties, response.getBytes());
      channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
      System.out.println("[rpc] server callback '" + response + "'");
      synchronized (monitor) {
        monitor.notify();
      }
    };

    channel.basicConsume(RPC_REQUEST_QUEUE, false, deliverCallback, consumerTag -> {});
    while (true) {
      synchronized (monitor) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static String work(String msg) {
    int i = Integer.parseInt(msg);
    return "" + 2 * i ;
  }
}
