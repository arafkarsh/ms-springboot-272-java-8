/**
 * (C) Copyright 2023 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.adapters.events.core;

// SpringBoot
import io.fusion.air.microservice.server.config.KafkaPubSubConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
// Java
import java.util.HashMap;
import java.util.Map;
// Kafka
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * Kafka Producer Configuration
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
public class KafkaProducerConfig {

    @Autowired
    private KafkaPubSubConfig kafkaPubSubConfig;

    /**
     * Create Kafka Template with Acks from All the Replicas
     * @return
     */
    @Bean
    @Qualifier("kafkaTemplateAcksAll")
    public KafkaTemplate<String, String> kafkaTemplateAcksAll() {
        return new KafkaTemplate<>(producerFactoryAcksAll());
    }

    /**
     * Create Kafka Template with Leader Ack
     * @return
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAcksOne() {
        return new KafkaTemplate<>(producerFactoryAcksOne());
    }

    /**
     * Create Kafka Template with Zero Acks
     * @return
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplateAcksZero() {
        return new KafkaTemplate<>(producerFactoryAcksZero());
    }

    /**
     * Create Producer Factory with Ack from All the Replicas
     * @return
     */
    @Bean
    public ProducerFactory<String, String> producerFactoryAcksAll() {
        return createProducerFactory("all");
    }

    /**
     * Create Producer Factory with Leader Acknowledgement
     * @return
     */
    @Bean
    public ProducerFactory<String, String> producerFactoryAcksOne() {
        return createProducerFactory("1");
    }

    /**
     * Create Producer Factory with ZERO Acknowledgement
     * @return
     */
    @Bean
    public ProducerFactory<String, String> producerFactoryAcksZero() {
        return createProducerFactory("0");
    }

    /**
     * Create Kafka Producer Factory with Ack Type as Input
     *
     * Ack Types
     *  ALL = All the Replicas needs to be committed
     *  1   = Leader Must Acknowledge
     *  0   = No Acknowledgement required
     *
     * @param acks
     * @return
     */
    private ProducerFactory<String, String> createProducerFactory(String acks) {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPubSubConfig.getKafkaServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, acks);

        return new DefaultKafkaProducerFactory<>(config);
    }
}
