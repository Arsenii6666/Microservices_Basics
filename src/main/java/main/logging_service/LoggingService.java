package main.logging_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
public class LoggingService {

    private Map<UUID, String> messageMap = new HashMap<>();

    @PostMapping("/log-message")
    public String logMessage(@RequestBody Map<String, String> payload) {
        UUID uuid = UUID.fromString(payload.get("uuid"));
        String message = payload.get("msg");

        // Store message and UUID in local hash map
        messageMap.put(uuid, message);

        // Log the message
        System.out.println("Received message: " + message);

        return "Message logged successfully";
    }

    @GetMapping("/get-all-messages")
    public String getAllMessages() {
        // Return all messages as a string
        // ...

        return "All messages: " + messageMap.values();
    }

    public static void main(String[] args) {
        SpringApplication.run(LoggingService.class, args);
    }
}

