package com.ifpb.pdist;

import com.rabbitmq.client.*;

public class Consumidor {
    private final static String QUEUE_NAME = "fila_duravel_persistente";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); 
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            long startTime = System.currentTimeMillis(); // Tempo inicial
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                long currentTime = System.currentTimeMillis();
                long sentTime = Long.parseLong(message.split("-")[1]);
                System.out.println("Diferença de tempo: " + (currentTime - sentTime) + " ms");
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

            Thread.sleep(10000); // Tempo para consumir mensagens
            long endTime = System.currentTimeMillis(); // Tempo final
            System.out.println("Tempo total de consumo: " + (endTime - startTime) + " ms");
        }
    }
}
