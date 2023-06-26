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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaProducerAcksByNone  implements  KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerAcksByNone(@Qualifier("kafkaTemplateAcksZero")
                                KafkaTemplate<String, String> kafkaTemplateAcksZero) {
        this.kafkaTemplate = kafkaTemplateAcksZero;
    }

    /**
     * Send the Message (data) to the Topic
     * @param topic
     * @param data
     */
    public void sendMessage(String topic,  String data) {
        this.kafkaTemplate.send(topic, null, data);
    }

    /**
     * Send the Message (data) to the Topic with Partition Key
     * @param topic
     * @param key
     * @param data
     */
    public void sendMessage(String topic, String key, String data) {
        this.kafkaTemplate.send(topic, key, data);
    }
}
