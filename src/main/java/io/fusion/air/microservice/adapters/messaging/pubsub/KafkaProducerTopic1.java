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
package io.fusion.air.microservice.adapters.messaging.pubsub;

// Custom
import io.fusion.air.microservice.adapters.messaging.core.KafkaProducerService;
import io.fusion.air.microservice.adapters.messaging.core.KafkaProducerTemplate;
import io.fusion.air.microservice.server.config.KafkaConfig;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Kafka Producer for Topic 1
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaProducerTopic1 {

    @Autowired
    private KafkaProducerTemplate kafkaTemplate;

    @Autowired
    private KafkaConfig kafkaConfig;


    private KafkaProducerService getKafkaTemplate(String _ackType) {
        return  kafkaTemplate.getKafkaTemplate(_ackType);
    }

    /**
     * Send Message to Kafka Topic 1 by AckType
     *
     * @param _message
     */
    public void sendMessage(String _message) {
        getKafkaTemplate(kafkaConfig.getKafkaTopic1AckType())
                .sendMessage(kafkaConfig.getKafkaTopic1(), null, _message);
    }


    /**
     * Send Message to the Kafka Topic 1 By theAck Type with Partition Key
     *
     * @param
     * @param _key
     * @param _message
     */
    public void sendMessage(String _key, String _message) {
        getKafkaTemplate(kafkaConfig.getKafkaTopic1AckType())
                .sendMessage(kafkaConfig.getKafkaTopic1(), _key, _message);
    }
}