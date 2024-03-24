package main.messages_service;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import main.logging_service.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.ServerSocket;
import java.util.Collections;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MessagesService {
    public static HazelcastInstance hz;
    private static Integer ServerPort;
    @Autowired
    private Environment environment;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LoggingService.class);
        ServerPort= 8100;
        Boolean PortIsFree=false;
        while (!PortIsFree){
            try (ServerSocket ignored = new ServerSocket(ServerPort)) {
                PortIsFree=true;
                app.setDefaultProperties(Collections.singletonMap("server.port", ServerPort));
            } catch (Exception ignored) {
                ServerPort++;
            }
        }
        Config LoggingServiceConfig = new Config();
        LoggingServiceConfig.setClusterName("MessagesServiceQueue");
        LoggingServiceConfig.getNetworkConfig().setPort(5702);
        LoggingServiceConfig.getNetworkConfig().getRestApiConfig().setEnabled(true);
        hz = Hazelcast.newHazelcastInstance(LoggingServiceConfig);
        app.run(args);
    }
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("MessagesService is running on port:" + ServerPort);
        int port = hz.getConfig().getNetworkConfig().getPort();
        System.out.println("Hazelcast is running on port: " + port);
    }
}

