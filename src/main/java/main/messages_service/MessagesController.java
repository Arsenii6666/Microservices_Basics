package main.messages_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesController {
    @GetMapping("/get-message")
    public String getStaticMessage() {
        System.out.println("Messages returned");
        return "Messages Service not implemented yet";
    }
}
