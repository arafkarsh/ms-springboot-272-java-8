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
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.fusion.air.microservice.domain.exceptions.StreamException;
import io.fusion.air.microservice.server.config.KafkaStreamsConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Configuration
@EnableKafkaStreams
public class ProductGlobalTableProcessor {

    @Autowired
    private KafkaStreamsConfig kafkaStreamsConfig;
    @Autowired
    private  ObjectMapper objectMapper;

    @ConditionalOnProperty(
            name = "spring.kafka.streams.auto-startup",
            havingValue = "true",
            matchIfMissing = false)
    @Bean
    public GlobalKTable<String, String> incrementVersionAndCreateGlobalTable(StreamsBuilder streamsBuilder) {
        String inputTopic2 = kafkaStreamsConfig.getStreamTopic2();
        String outputTopic3 = kafkaStreamsConfig.getStreamTopic3();
        System.out.println("GLOBAL KTable >>>>> 1");
        final Serde<JsonNode> jsonNodeSerde = new JsonNodeSerde();
        KStream<String, JsonNode> inputStream = streamsBuilder.stream(inputTopic2,
                Consumed.with(Serdes.String(), jsonNodeSerde));
        KStream<String, String> rekeyedStream = inputStream.map((key, value) -> {
            System.out.println("GT>> reKey >> "+value);
            String newKey = value.get("uuid").asText();  // Assuming the message key format you provided
            return new KeyValue<>(newKey, value.toString());
        });
        KTable<String, String> kTable = rekeyedStream.groupByKey().aggregate(
                () -> null,
                (key, newValue, aggValue) -> {
                    try {
                        ObjectNode newObjectB = (ObjectNode) objectMapper.readTree(newValue);
                        if (aggValue != null) {
                            ObjectNode oldObjectB = (ObjectNode) objectMapper.readTree(aggValue);
                            int oldVersion = oldObjectB.get("version").asInt();
                            newObjectB.put("version", oldVersion + 1);
                        } else {
                            newObjectB.put("version", 1);
                        }
                        System.out.println("GLOBAL KTable >>> GT = "+newObjectB);
                        return newObjectB.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new StreamException("Unable to Parse JSON > ", e);
                    }
                }
        );

        System.out.println("GLOBAL KTable >>>>> 2 outputTopic3");
        // Convert the KTable back to a KStream and then send to a new topic
        kTable.toStream().to(outputTopic3);

        System.out.println("GLOBAL KTable >>>>> 3 globalKTable");
        // Create a GlobalKTable using the new output topic
        GlobalKTable<String, String> globalKTable = streamsBuilder
                .globalTable(outputTopic3, Materialized.as("products-store"));

        return globalKTable;
    }

    /**
     * Deprecated Code
    public GlobalKTable<String, String> incrementVersionAndCreateGlobalTableOld(StreamsBuilder streamsBuilder) {
        String inputTopic2 = kafkaStreamsConfig.getStreamTopic2();
        String outputTopic3 = kafkaStreamsConfig.getStreamTopic3();
        System.out.println("GLOBAL KTable >>>>> 1");
        KStream<String, String> inputStream = streamsBuilder.stream(inputTopic2, Consumed.with(Serdes.String(), Serdes.String()));
        KTable<String, String> kTable = inputStream.groupByKey().aggregate(
                () -> null,
                (key, newValue, aggValue) -> {
                    try {
                        ObjectNode newObjectB = (ObjectNode) objectMapper.readTree(newValue);
                        if (aggValue != null) {
                            ObjectNode oldObjectB = (ObjectNode) objectMapper.readTree(aggValue);
                            int oldVersion = oldObjectB.get("version").asInt();
                            newObjectB.put("version", oldVersion + 1);
                        } else {
                            newObjectB.put("version", 1);
                        }
                        System.out.println("GLOBAL KTable >>> GT = "+newObjectB);
                        return newObjectB.toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new StreamException("Unable to Parse JSON > ", e);
                    }
                }
        );

        System.out.println("GLOBAL KTable >>>>> 2 outputTopic3");
        // Convert the KTable back to a KStream and then send to a new topic
        kTable.toStream().to(outputTopic3);

        System.out.println("GLOBAL KTable >>>>> 3 globalKTable");
        // Create a GlobalKTable using the new output topic
        GlobalKTable<String, String> globalKTable = streamsBuilder
                .globalTable(outputTopic3, Materialized.as("products-store"));

        return globalKTable;
    }
     */

}
