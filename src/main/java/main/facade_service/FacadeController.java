package main.facade_service;

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
import java.util.Map;
import java.util.UUID;

@RestController
public class FacadeController {
    private RestTemplate restTemplate;
    @Autowired
    public void MyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @PostMapping("/send-message")
    public String sendMessage(@RequestBody String message) {
        UUID uuid = UUID.randomUUID();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> payload = new HashMap<>();
        payload.put("uuid", uuid.toString());
        payload.put("msg", message);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(payload, headers);
        String loggingServiceUrl = "http://localhost:8082/log-message";
        String response = restTemplate.postForObject(loggingServiceUrl, requestEntity, String.class);
        return "Message sent with UUID: " + uuid + ", Result: " + response;
    }
    @GetMapping("/get-messages")
    public String getMessages() {
        String loggingResult = restTemplate.getForObject("http://localhost:8082/get-all-messages", String.class);
        String MessagesResult = restTemplate.getForObject("http://localhost:8083/get-message", String.class);
        return "Logging:"+loggingResult+"\n"+"Messages:"+MessagesResult+"\n";
    }

}
