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

import io.fusion.air.microservice.server.config.KafkaPubSubConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Set;

import java.util.concurrent.ExecutionException;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Create Kafka Topic 1
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class KafkaTopic1Creator {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private KafkaPubSubConfig kafkaPubSubConfig;
    @Autowired
    private AdminClient adminClient;

    /**
     * Create the Kafka Topic
     */
    @PostConstruct
    public void initialize() {
        if(kafkaPubSubConfig.isCreateKafkaTopic1()) {
            createTopic();
        }
    }

    /**
     * Create the Kafka Topic with Partitions and Replica Sets
     */
    private void createTopic() {
        try {
            log.info("Creating Kafka Topic Name="+ kafkaPubSubConfig.getKafkaTopic1()
                    + " Partitions="+ kafkaPubSubConfig.getKafkaTopic1Partitions()
                    + " Replicas="+ kafkaPubSubConfig.getKafkaTopic1Replica()
            );
            ListTopicsResult listTopics = adminClient.listTopics();
            Set<String> names = listTopics.names().get();
            if (!names.contains(kafkaPubSubConfig.getKafkaTopic1())) {
                NewTopic newTopic = new NewTopic(
                                        kafkaPubSubConfig.getKafkaTopic1(),             // Topic Name
                                        kafkaPubSubConfig.getKafkaTopic1Partitions(),   // No. of Partitions
                                        kafkaPubSubConfig.getKafkaTopic1Replica()       // No. of Replicas
                );
                adminClient.createTopics(Collections.singletonList(newTopic));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
