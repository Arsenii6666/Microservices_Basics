package main.facade_service;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
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
    private List<Integer> messagesServicePorts;
    private static HazelcastInstance hz;
    @Autowired
    public FacadeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.loggingServicePorts = FacadeService.LOGGING_PORTS;
        this.messagesServicePorts = FacadeService.MASSAGES_PORTS;
        hz=FacadeService.hz;
    }
    @PostMapping("/send-message")
    public String sendMessage(@RequestBody String message) {
        updateServicePorts();
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
        IQueue<String> messageQueue = hz.getQueue("message-queue");
        messageQueue.add(payload.toString());
        System.out.println("Message sent with UUID: " + uuid + ", Result: " + response);
        return "Message sent with UUID: " + uuid + ", Result: " + response;
    }

    @GetMapping("/get-messages")
    public String getMessages() {
        updateServicePorts();
        if (loggingServicePorts.isEmpty()) {
            return "No available logging services.";
        }
        int firstAvailableLoggingService = loggingServicePorts.get(0);
        int randomAvailableMassageService = messagesServicePorts.get(new Random().nextInt(messagesServicePorts.size()));
        String loggingResult = restTemplate.getForObject("http://localhost:" + firstAvailableLoggingService + "/get-all-messages", String.class);
        String messagesResult = restTemplate.getForObject("http://localhost:"+randomAvailableMassageService+"/get-message", String.class);
        System.out.println("Logging: "+loggingResult+"\n"+"Messages: "+messagesResult+"\n");
        return "Logging: "+loggingResult+"\n"+"Messages: "+messagesResult+"\n";
    }
    private void updateServicePorts() {
        loggingServicePorts = FacadeService.searchLoggingServices();
        messagesServicePorts = FacadeService.searchMassageServices();
    }
}
