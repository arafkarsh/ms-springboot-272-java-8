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
package io.fusion.air.microservice.adapters.messaging.services;

import io.fusion.air.microservice.adapters.messaging.core.KafkaProducerAcksByLeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaProducer {

    @Autowired
    private KafkaProducerAcksByLeader kafkaTemplate;

    /**
     * Send Message to Kafka Topic With Ack by the Leader ONLY
     *
     * @param _topic
     * @param _message
     */
    public void sendMessage(String _topic, String _message) {
        this.kafkaTemplate.sendMessage(_topic, null, _message);
    }

    /**
     * Send Message to the Kafka Topic with Ack By the Leader with Partition Key
     *
     * @param
     * @param _key
     * @param _message
     */
    public void sendMessage(String _topic, String _key, String _message) {
        this.kafkaTemplate.sendMessage(_topic, _key, _message);
    }
}
