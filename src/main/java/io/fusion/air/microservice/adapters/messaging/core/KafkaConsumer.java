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
package io.fusion.air.microservice.adapters.messaging.core;

import io.fusion.air.microservice.adapters.controllers.KafkaRestController;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaConsumer {

    @Autowired
    private ServiceConfiguration serviceConfiguration;

    /**
     * Kafka Consumer for Topic 1 (As per the Configuration in the Properties file)
     * AutoStart is disabled to testing purpose ONLY.
     * In a real world scenario autostart will be TRUE
     *
     * There is KafkaRestController to start and stop the @KafkaListener. This is
     * also for the Demo Purpose ONLY
     * @see KafkaRestController
     * @param message
     */
    @KafkaListener(id = "fusionListenerT1", autoStartup = "false",
            topics = "#{serviceConfiguration.getKafkaTopic1()}",
            groupId = "#{serviceConfiguration.getKafkaConsumerGroup()}")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }

}
