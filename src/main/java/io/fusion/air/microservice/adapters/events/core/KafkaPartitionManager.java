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

import io.fusion.air.microservice.domain.exceptions.MessagingException;
import io.fusion.air.microservice.server.config.KafkaPubSubConfig;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewPartitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;


/**
 * Change the Kafka Topic Partitions Dynamically
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaPartitionManager {

    @Autowired
    private KafkaPubSubConfig kafkaPubSubConfig;

    public void increasePartitions(String topicName, int newPartitionCount) {
        // Configure the AdminClient with your bootstrap servers
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPubSubConfig.getKafkaServers());

        try (AdminClient adminClient = AdminClient.create(config)) {
            // Create a NewPartitions instance with newPartitionCount
            NewPartitions newPartitions = NewPartitions.increaseTo(newPartitionCount);

            // Use the AdminClient to apply this to the topic
            adminClient.createPartitions(Collections.singletonMap(topicName, newPartitions)).all().get();
        } catch (Exception e) {
            throw new MessagingException("Unable to increase the Partitions for the Kafka Topic "+topicName);
        }
    }
}
