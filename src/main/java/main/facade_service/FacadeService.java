package main.facade_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SpringBootApplication
@RestController
public class FacadeService {

    @PostMapping("/send-message")
    public String sendMessage(@RequestBody String message) {
        UUID uuid = UUID.randomUUID();
        // Send message and UUID to logging-service
        // ...

        return "Message sent with UUID: " + uuid;
    }

    @GetMapping("/get-messages")
    public String getMessages() {
        // Send GET requests to logging-service and messages-service
        // ...

        // Concatenate and return the responses
        // ...

        return "Response from get-messages endpoint";
    }

    public static void main(String[] args) {
        SpringApplication.run(FacadeService.class, args);
    }
}
