package main.facade_service;

import main.logging_service.LoggingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FacadeService {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FacadeService.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        app.run(args);
    }
}
