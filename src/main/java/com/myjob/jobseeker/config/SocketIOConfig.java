package com.myjob.jobseeker.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost"); // Or your server's IP
        config.setPort(9092); // Choose an available port

        // CORS settings
        config.setOrigin("*");

        // Socket settings
        config.getSocketConfig().setReuseAddress(true);

        SocketIOServer server = new SocketIOServer(config);
        // Add event listeners here
        server.addEventListener("message", String.class, (client, data, ackSender) -> {
            System.out.println("Received message from client: " + data);
            client.sendEvent("message", "Server received your message: " + data);
        });
        return server;
    }
}
