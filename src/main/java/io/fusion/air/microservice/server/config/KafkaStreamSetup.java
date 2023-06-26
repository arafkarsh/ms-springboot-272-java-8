package io.fusion.air.microservice.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.kafka.streams.StreamsConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */


@Configuration
public class KafkaStreamSetup {
    @Bean(name = "kafkaStreamsSettings")
    public Map<String, Object> kafkaStreamsSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "app-id");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Other Kafka Streams settings...
        return settings;
    }
}