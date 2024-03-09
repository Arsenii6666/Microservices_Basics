package main.facade_service;

import main.logging_service.LoggingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FacadeService {
    public static List<Integer> LOGGING_PORTS;
    private static final int MIN_LOGGING_PORT = 8090;
    private static final int MAX_LOGGING_PORT = 8095;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FacadeService.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        LOGGING_PORTS=searchLoggingServices();
        app.run(args);
    }
    public static List<Integer> searchLoggingServices() {
        List<Integer> occupiedPorts = new ArrayList<>();
        for (int port = MIN_LOGGING_PORT; port <= MAX_LOGGING_PORT; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                continue;
            } catch (IOException e) {
                occupiedPorts.add(port);;
            }
        }
        return occupiedPorts;
    }
}
