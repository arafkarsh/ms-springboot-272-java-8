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

import io.fusion.air.microservice.server.config.KafkaStreamsConfig;
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
 * Create Kafka Streams Topic 1,2,3
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class KafkaStreamsTopicsCreator {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private KafkaStreamsConfig kafkaConfig;
    @Autowired
    private AdminClient adminClient;

    /**
     * Create the Kafka Topic
     */
    @PostConstruct
    public void initialize() {
        createStreamsTopic1();
        createStreamsTopic2();
        createStreamsTopic3();
    }

    /**
     * Create the Kafka Stream Topic 1 with Partitions and Replica Sets
     */
    private void createStreamsTopic1() {
        try {
            if(kafkaConfig.isCreateKafkaStreamsTopic1()) {
                log.info("Creating Kafka Streams Topic Name=" + kafkaConfig.getStreamTopic1()
                        + " Partitions=" + kafkaConfig.getKafkaStreamsTopic1Partitions()
                        + " Replicas=" + kafkaConfig.getKafkaStreamsTopic1Replica()
                );
                ListTopicsResult listTopics = adminClient.listTopics();
                Set<String> names = listTopics.names().get();
                if (!names.contains(kafkaConfig.getStreamTopic1())) {
                    NewTopic newTopic = new NewTopic(
                            kafkaConfig.getStreamTopic1(),                      // Topic Name
                            kafkaConfig.getKafkaStreamsTopic1Partitions(),   // No. of Partitions
                            kafkaConfig.getKafkaStreamsTopic1Replica()       // No. of Replicas
                    );
                    adminClient.createTopics(Collections.singletonList(newTopic));
                } else {
                    log.info("Kafka Streams (Exists) Topic Name=" + kafkaConfig.getStreamTopic1()
                            + " Partitions=" + kafkaConfig.getKafkaStreamsTopic1Partitions()
                            + " Replicas=" + kafkaConfig.getKafkaStreamsTopic1Replica()
                    );
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create the Kafka Stream Topic 2 with Partitions and Replica Sets
     */
    private void createStreamsTopic2() {
        try {
            if(kafkaConfig.isCreateKafkaStreamsTopic2()) {
                log.info("Creating Kafka Streams Topic Name=" + kafkaConfig.getStreamTopic2()
                        + " Partitions=" + kafkaConfig.getKafkaStreamsTopic2Partitions()
                        + " Replicas=" + kafkaConfig.getKafkaStreamsTopic2Replica()
                );
                ListTopicsResult listTopics = adminClient.listTopics();
                Set<String> names = listTopics.names().get();
                if (!names.contains(kafkaConfig.getStreamTopic2())) {
                    NewTopic newTopic = new NewTopic(
                            kafkaConfig.getStreamTopic2(),                      // Topic Name
                            kafkaConfig.getKafkaStreamsTopic2Partitions(),   // No. of Partitions
                            kafkaConfig.getKafkaStreamsTopic2Replica()       // No. of Replicas
                    );
                    adminClient.createTopics(Collections.singletonList(newTopic));
                } else {
                    log.info("Kafka Streams (Exists) Topic Name=" + kafkaConfig.getStreamTopic2()
                            + " Partitions=" + kafkaConfig.getKafkaStreamsTopic2Partitions()
                            + " Replicas=" + kafkaConfig.getKafkaStreamsTopic2Replica()
                    );
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create the Kafka Stream Topic 3 with Partitions and Replica Sets
     */
    private void createStreamsTopic3() {
        try {
            if(kafkaConfig.isCreateKafkaStreamsTopic3()) {
                log.info("Creating Kafka Streams Topic Name=" + kafkaConfig.getStreamTopic3()
                        + " Partitions=" + kafkaConfig.getKafkaStreamsTopic3Partitions()
                        + " Replicas=" + kafkaConfig.getKafkaStreamsTopic3Replica()
                );
                ListTopicsResult listTopics = adminClient.listTopics();
                Set<String> names = listTopics.names().get();
                if (!names.contains(kafkaConfig.getStreamTopic3())) {
                    NewTopic newTopic = new NewTopic(
                            kafkaConfig.getStreamTopic3(),                      // Topic Name
                            kafkaConfig.getKafkaStreamsTopic3Partitions(),   // No. of Partitions
                            kafkaConfig.getKafkaStreamsTopic3Replica()       // No. of Replicas
                    );
                    adminClient.createTopics(Collections.singletonList(newTopic));
                } else {
                    log.info("Kafka Streams (Exists) Topic Name=" + kafkaConfig.getStreamTopic3()
                            + " Partitions=" + kafkaConfig.getKafkaStreamsTopic3Partitions()
                            + " Replicas=" + kafkaConfig.getKafkaStreamsTopic3Replica()
                    );
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
