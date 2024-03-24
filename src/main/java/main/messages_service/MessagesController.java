package main.messages_service;

import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import main.facade_service.FacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MessagesController {

    private static final Map<String, String> messageMap = new HashMap<>();

    @Autowired
    public MessagesController(RestTemplate restTemplate) {
        HazelcastInstance hz = MessagesService.hz;
        IQueue<String> messageQueue = hz.getQueue("message-queue");
        new Thread(() -> {
            while (true) {
                try {
                    String payloadString = messageQueue.take();
                    Map<String, String> payload = parsePayloadString(payloadString);
                    messageMap.put(payload.get("uuid"), payload.get("msg"));
                    System.out.println("Received message from queue: " + payload.get("msg"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @GetMapping("/get-message")
    public String getStaticMessage() {
        System.out.println("Messages returned");
        return messageMap.toString();
    }
    private Map<String, String> parsePayloadString(String payloadString) {
        Map<String, String> payloadMap = new HashMap<>();
        String[] parts = payloadString.split(";\n/");
        payloadMap.put("uuid", parts[0]);
        payloadMap.put("msg", parts[1]);
        return payloadMap;
    }

}
