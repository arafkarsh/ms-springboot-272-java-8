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
package io.fusion.air.microservice.adapters.events.streams;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// Custom
import io.fusion.air.microservice.domain.exceptions.StreamException;
import io.fusion.air.microservice.server.config.KafkaStreamsConfig;
// Apache Kafka
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
// Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Kafka Streams to Transform the Data
 * Transform Product JSON in ms_cache_272.ms_schema.products_m
 * To ms_cache_272.ms_schema.products_data
 * In this transformation only payload (actual data) will be stored in *.products_data topic
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
@Component
public class ProductDBStreamProcessor {

    @Autowired
    private KafkaStreamsConfig kafkaStreamsConfig;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ConditionalOnProperty(
            name = "spring.kafka.streams.auto-startup",
            havingValue = "true",
            matchIfMissing = false)
    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {

        KStream<String, String> inputStream = streamsBuilder
                .stream(kafkaStreamsConfig.getStreamTopic1(),
                Consumed.with(Serdes.String(), Serdes.String()));

        // Topic 1 >> ms_cache_272.ms_schema.products_m
        // From the Raw data Extract the Payload (Product Record) and Store
        // Store it in Stream Topic 2 >> ms_cache_272.ms_schema.products_data
        KStream<String, String> outputStream = inputStream.mapValues(value -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(value);
                JsonNode payloadNode = jsonNode.get("payload");

                System.out.println(payloadNode.toString());

                return payloadNode.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new StreamException("Unable to Parse Raw Product JSON > ",e);
            }
        });
        // Store the Extracted data in Stream Topic 2 >> ms_cache_272.ms_schema.products_data
        outputStream.to(kafkaStreamsConfig.getStreamTopic2());

        return outputStream;
    }

}