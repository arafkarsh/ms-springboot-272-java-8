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

import io.fusion.air.microservice.adapters.controllers.KafkaListenerController;
import io.fusion.air.microservice.domain.exceptions.MessagingException;
import io.fusion.air.microservice.server.config.KafkaPubSubConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Topic 1
 * Kafka Consumer with Server Sent Event (SSE) Emitter
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class ConsumerTopic1 {

    @Autowired
    private KafkaPubSubConfig kafkaPubSubConfig;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    /**
     * Kafka Consumer for Topic 1 (As per the Configuration in the Properties file)
     * AutoStart is disabled for testing purpose ONLY.
     * In a real world scenario autostart will be TRUE
     *
     * There is KafkaListenerController to start and stop the @KafkaListener. This is
     * also for the Demo Purpose ONLY
     * @see KafkaListenerController
     *
     * @param record
     * @param acknowledgment
     */
    @KafkaListener(id = "fusionListenerT1", autoStartup = "true",
            topics = "#{kafkaPubSubConfig.getKafkaTopic1()}",
            groupId = "#{kafkaPubSubConfig.getKafkaConsumerGroup1()}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        try {
            // 1. Read The message
            System.out.println("T1>> Received message: " + record.value()
                    + ", from partition/offset: " + record.partition() + "/" + record.offset());
            // 2. Do Message processing
            // 3. Ack the Message
            acknowledgment.acknowledge();
            // IF REQUIRED - FOR TESTING PURPOSE ONLY
            sendMessageToRestClients(record.value().toString());
        } catch (Exception e) {
            // Dead Letter Queue
            // 1. (Recommended) Implement the Dead Letter Queue Outside Kafka.
            // 2. Implement a Dead Letter Queue in a Kafka Topic with a Single Partition. if that Node
            //    goes down then you will lose all the data.
            throw new MessagingException("KafkaListener Error : "+e.getMessage());
        }
    }

    // ====================================================================================================
    // TO SUPPORT REST CLIENTS
    // ====================================================================================================
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
     * FOR DEMO PURPOSE ONLY
     * For Sending the Message to a Rest Client
     * @param _message
     */
    private void sendMessageToRestClients(String _message) {
        if(_message == null) return;
        for (final SseEmitter emitter : emitters) {
            try {
                emitter.send(_message);
            } catch (Exception e) {
                // If we fail to send the message, remove the emitter from the list
                removeEmitter(emitter);
            }
        }
    }

}
