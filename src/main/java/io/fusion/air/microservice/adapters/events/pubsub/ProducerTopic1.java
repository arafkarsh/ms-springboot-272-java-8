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
package io.fusion.air.microservice.adapters.events.pubsub;

// Custom
import io.fusion.air.microservice.adapters.events.core.KafkaProducerService;
import io.fusion.air.microservice.adapters.events.core.KafkaProducerTemplate;
import io.fusion.air.microservice.server.config.KafkaPubSubConfig;
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
public class ProducerTopic1 {

    @Autowired
    private KafkaProducerTemplate kafkaTemplate;

    @Autowired
    private KafkaPubSubConfig kafkaPubSubConfig;


    private KafkaProducerService getKafkaTemplate(String _ackType) {
        return  kafkaTemplate.getKafkaTemplate(_ackType);
    }

    /**
     * Send Message to Kafka Topic 1 by AckType
     *
     * @param _message
     */
    public void sendMessage(String _message) {
        getKafkaTemplate(kafkaPubSubConfig.getKafkaTopic1AckType())
                .sendMessage(kafkaPubSubConfig.getKafkaTopic1(), null, _message);
    }


    /**
     * Send Message to the Kafka Topic 1 By theAck Type with Partition Key
     *
     * @param
     * @param _key
     * @param _message
     */
    public void sendMessage(String _key, String _message) {
        getKafkaTemplate(kafkaPubSubConfig.getKafkaTopic1AckType())
                .sendMessage(kafkaPubSubConfig.getKafkaTopic1(), _key, _message);
    }
}
