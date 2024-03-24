package main.facade_service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import main.logging_service.LoggingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
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
    public static List<Integer> MASSAGES_PORTS;
    private static final int MIN_MASSAGES_PORT = 8100;
    private static final int MAX_MASSAGES_PORT = 8105;
    public static HazelcastInstance hz;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FacadeService.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
        LOGGING_PORTS=searchLoggingServices();
        MASSAGES_PORTS=searchMassageServices();
        Config LoggingServiceConfig = new Config();
        LoggingServiceConfig.setClusterName("MessagesServiceQueue");
        LoggingServiceConfig.getNetworkConfig().setPort(5702);
        LoggingServiceConfig.getNetworkConfig().getRestApiConfig().setEnabled(true);
        hz = Hazelcast.newHazelcastInstance(LoggingServiceConfig);
        app.run(args);
    }
    private static List<Integer> searchServices(int min_port, int max_port) {
        List<Integer> occupiedPorts = new ArrayList<>();
        for (int port = min_port; port <= max_port; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                continue;
            } catch (IOException e) {
                occupiedPorts.add(port);;
            }
        }
        return occupiedPorts;
    }
    public static List<Integer> searchLoggingServices(){
        return searchServices(MIN_LOGGING_PORT, MAX_LOGGING_PORT);
    }
    public static List<Integer> searchMassageServices(){
        return searchServices(MIN_MASSAGES_PORT, MAX_MASSAGES_PORT);
    }
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("FacadeService is running on port:" + "8081");
        int port = hz.getConfig().getNetworkConfig().getPort();
        System.out.println("Hazelcast is running on port: " + port);
    }
}
