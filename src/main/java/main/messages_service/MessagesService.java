package main.messages_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MessagesService {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MessagesService.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
        app.run(args);
    }
}

