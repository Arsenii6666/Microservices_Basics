package main.facade_service;

import main.logging_service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

@RestController
public class FacadeController {

    private final RestTemplate restTemplate;
    private List<Integer> loggingServicePorts;
    @Autowired
    public FacadeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.loggingServicePorts = FacadeService.LOGGING_PORTS;
    }
    @PostMapping("/send-message")
    public String sendMessage(@RequestBody String message) {
        updateLoggingServicePorts();
        if (loggingServicePorts.isEmpty()) {
            return "No available logging services.";
        }
        UUID uuid = UUID.randomUUID();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = new HashMap<>();
        payload.put("uuid", uuid.toString());
        payload.put("msg", message);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);
        int randomPort = loggingServicePorts.get(new Random().nextInt(loggingServicePorts.size()));
        String loggingServiceUrl = "http://localhost:" + randomPort + "/log-message";
        String response = restTemplate.postForObject(loggingServiceUrl, requestEntity, String.class);
        System.out.println("Message sent with UUID: " + uuid + ", Result: " + response);
        return "Message sent with UUID: " + uuid + ", Result: " + response;
    }

    @GetMapping("/get-messages")
    public String getMessages() {
        updateLoggingServicePorts();
        if (loggingServicePorts.isEmpty()) {
            return "No available logging services.";
        }
        int firstAvailablePort = loggingServicePorts.get(0);
        String loggingResult = restTemplate.getForObject("http://localhost:" + firstAvailablePort + "/get-all-messages", String.class);
        String MessagesResult = restTemplate.getForObject("http://localhost:8082/get-message", String.class);
        System.out.println("Logging: "+loggingResult+"\n"+"Messages: "+MessagesResult+"\n");
        return "Logging: "+loggingResult+"\n"+"Messages: "+MessagesResult+"\n";
    }
    private void updateLoggingServicePorts() {
        loggingServicePorts = FacadeService.searchLoggingServices();
    }
}
