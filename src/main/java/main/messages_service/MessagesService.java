package main.messages_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MessagesService {

    @GetMapping("/get-static-message")
    public String getStaticMessage() {
        // Return a static message
        return "not implemented yet";
    }

    public static void main(String[] args) {
        SpringApplication.run(MessagesService.class, args);
    }
}

