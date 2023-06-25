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

import io.fusion.air.microservice.adapters.controllers.KafkaListenerController;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Topic 2
 * Kafka Consumer with Server Sent Event (SSE) Emitter
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaConsumerTopic2 {

    @Autowired
    private ServiceConfiguration serviceConfiguration;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Add The SSE (Server Sent Event) Emitter
     * @param emitter
     */
    public void addEmitter(final SseEmitter emitter) {
        emitters.add(emitter);
    }

    /**
     * Remove The SSE (Server Sent Event) Emitter
     * @param emitter
     */
    public void removeEmitter(final SseEmitter emitter) {
        emitters.remove(emitter);
    }

    /**
     * Kafka Consumer for Topic 2 (As per the Configuration in the Properties file)
     * AutoStart is disabled to testing purpose ONLY.
     * In a real world scenario autostart will be TRUE
     *
     * There is KafkaListenerController to start and stop the @KafkaListener. This is
     * also for the Demo Purpose ONLY
     * @see KafkaListenerController
     * @param message
     */
    @KafkaListener(id = "fusionListenerT2", autoStartup = "false",
            topics = "#{serviceConfiguration.getKafkaTopic2()}",
            groupId = "#{serviceConfiguration.getKafkaConsumerGroup()}")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
        for (final SseEmitter emitter : emitters) {
            try {
                emitter.send(message);
            } catch (Exception e) {
                // If we fail to send the message, remove the emitter from the list
                removeEmitter(emitter);
            }
        }
    }

}
