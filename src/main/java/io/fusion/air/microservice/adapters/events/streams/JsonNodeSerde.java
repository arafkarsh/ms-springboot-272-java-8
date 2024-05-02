/**
 * (C) Copyright 2024 Araf Karsh Hamid
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

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Serde;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Custom JSON Serializer for Kafka
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class JsonNodeSerde implements Serde<JsonNode> {
    private final JsonSerializer serializer = new JsonSerializer();
    private final JsonDeserializer deserializer = new JsonDeserializer();

    @Override
    public Serializer<JsonNode> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<JsonNode> deserializer() {
        return deserializer;
    }

    public static class JsonSerializer implements Serializer<JsonNode> {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public byte[] serialize(String topic, JsonNode data) {
            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (Exception e) {
                throw new IllegalStateException("Error serializing value", e);
            }
        }

        @Override
        public void close() {
        }
    }

    public static class JsonDeserializer implements Deserializer<JsonNode> {
        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public JsonNode deserialize(String topic, byte[] data) {
            try {
                return objectMapper.readTree(data);
            } catch (Exception e) {
                throw new IllegalStateException("Error deserializing value", e);
            }
        }

        @Override
        public void close() {
        }
    }
}
