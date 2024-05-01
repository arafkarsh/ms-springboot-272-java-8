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
package io.fusion.air.microservice.server.config;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.kafka.streams.StreamsConfig;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Streams Setups
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamSetup {

    @Autowired
    private KafkaStreamsConfig kafkaStreamsConfig;

    /**
     * Creates Streams Settings with Kafka Cluster Details
     * @return
     */
    @ConditionalOnProperty(
            name = "spring.kafka.streams.auto-startup",
            havingValue = "true",
            matchIfMissing = false)
    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaStreamsConfig.getSpringAppName());
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaStreamsConfig.getKafkaServers());
        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return new KafkaStreamsConfiguration(settings);
    }

    /**
     *
     * @param streamsBuilderFactoryBean
     * @return
     */
    @ConditionalOnProperty(
            name = "spring.kafka.streams.auto-startup",
            havingValue = "true",
            matchIfMissing = false)
    @Bean
    public SmartLifecycle autoStartKStreams(StreamsBuilderFactoryBean streamsBuilderFactoryBean) {
        return new SmartLifecycle() {
            @Override
            public void start() {
                streamsBuilderFactoryBean.start();
            }

            @Override
            public void stop() {
                streamsBuilderFactoryBean.stop();
            }

            @Override
            public boolean isRunning() {
                return streamsBuilderFactoryBean.isRunning();
            }
        };
    }
}