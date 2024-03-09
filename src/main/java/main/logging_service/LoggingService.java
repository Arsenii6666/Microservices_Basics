package main.logging_service;

import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.Collections;
import java.net.ServerSocket;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class LoggingService {
    public static HazelcastInstance hz;
    private static Integer ServerPort;
    @Autowired
    private Environment environment;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(LoggingService.class);
        ServerPort= 8090;
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
        LoggingServiceConfig.setClusterName("LoggingServiceMap");
        LoggingServiceConfig.getNetworkConfig().setPort(5701);
        LoggingServiceConfig.getNetworkConfig().getRestApiConfig().setEnabled(true);
        hz = Hazelcast.newHazelcastInstance(LoggingServiceConfig);
        app.run(args);
    }
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("LoggingService is running on port:" + ServerPort);
        int port = hz.getConfig().getNetworkConfig().getPort();
        System.out.println("Hazelcast is running on port: " + port);
    }
}

