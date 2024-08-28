package com.ifpb.pdist;

import com.rabbitmq.client.*;

public class Produtor {
    private final static String QUEUE_NAME = "fila_duravel_persistente";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); 
             Channel channel = connection.createChannel()) {
            long startTime = System.currentTimeMillis(); // Tempo inicial

            // Declarar a fila como durável
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            for (int i = 1; i <= 1000000; i++) {
                String message = i + "-" + System.currentTimeMillis();
                // Mensagem persistente
                channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            }

            long endTime = System.currentTimeMillis(); // Tempo final
            System.out.println("Tempo total de produção: " + (endTime - startTime) + " ms");
        }
    }
}
