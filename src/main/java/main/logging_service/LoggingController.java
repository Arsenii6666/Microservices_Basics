package main.logging_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class LoggingController {
    private final Map<UUID, String> messageMap;
    @Autowired
    public LoggingController(LoggingService loggingService) {
        messageMap = LoggingService.hz.getMap("LoggingServiceMap");
    }
    @PostMapping("/log-message")
    public String logMessage(@RequestBody Map<String, String> payload) {
        UUID uuid = UUID.fromString(payload.get("uuid"));
        String message = payload.get("msg");
        messageMap.put(uuid, message);
        System.out.println("Received message: " + message);
        return "Message logged successfully";

    }
    @GetMapping("/get-all-messages")
    public String getAllMessages() {
        return "All messages: " + messageMap.values();
    }
}
