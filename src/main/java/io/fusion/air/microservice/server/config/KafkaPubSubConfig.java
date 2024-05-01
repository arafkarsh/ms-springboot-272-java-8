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
package io.fusion.air.microservice.server.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Kafka Configuration
 *
 * @author arafkarsh
 *
 */
@Component
@Configuration
@PropertySource(
		name = "kafkaConfig",
		// Expects file in the directory the jar is executed
		value = "file:./application.properties")
		// Expects the file in src/main/resources folder
		// value = "classpath:application.properties")
		// value = "classpath:application2.properties,file:./application.properties")
public class KafkaPubSubConfig implements Serializable {

	// KAFKA Configurations

	@Value("${spring.application.name:ms-cache}")
	private String springAppName;

	@JsonIgnore
	@Value("${spring.kafka.bootstrap-servers:127.0.0.1:9092}")
	private String kafkaServers;

	@JsonIgnore
	@Value("${kafka.consumer.group.1:fusionGroup1}")
	private String kafkaConsumerGroup1;

	@JsonIgnore
	@Value("${kafka.consumer.group.2:fusionGroup2}")
	private String kafkaConsumerGroup2;

	@JsonIgnore
	// Create Kafka Topic 1
	@Value("${kafka.topic.1.create:false}")
	private boolean createKafkaTopic1;

	@JsonIgnore
	// Kafka Topic 1
	@Value("${kafka.topic.1}")
	private String kafkaTopic1;

	@JsonIgnore
	// Kafka Topic 1 and its Partitions
	@Value("${kafka.topic.1.partitions:5}")
	private int kafkaTopic1Partitions;

	@JsonIgnore
	// Kafka Topic 1 and its Replicas
	@Value("${kafka.topic.1.replica:1}")
	private short kafkaTopic1Replica;

	@JsonIgnore
	// Kafka Topic 1 with Ack Type (All, 1 & 0)
	@Value("${kafka.topic.1.acks:1}")
	private String kafkaTopic1AckType;

	@JsonIgnore
	// Create Kafka Topic 2
	@Value("${kafka.topic.2.create:false}")
	private boolean createKafkaTopic2;

	@JsonIgnore
	// Kafka Topic 2
	@Value("${kafka.topic.2}")
	private String kafkaTopic2;

	@JsonIgnore
	// Kafka Topic 2 and its Partitions
	@Value("${kafka.topic.2.partitions:7}")
	private int kafkaTopic2Partitions;

	@JsonIgnore
	// Kafka Topic 2 and its Replicas
	@Value("${kafka.topic.2.replica:1}")
	private short kafkaTopic2Replica;

	@JsonIgnore
	// Kafka Topic 1 with Ack Type (All, 1 & 0)
	@Value("${kafka.topic.2.acks:1}")
	private String kafkaTopic2AckType;

	/**
     * Returns the Kafka Servers
	 * @return
	 */
	public String getKafkaServers() {
		return kafkaServers;
	}

	/**
     * Returns the Kafka Consumer Group ID 1
	 * @return
	 */
	public String getKafkaConsumerGroup1() {
		return kafkaConsumerGroup1;
	}

	/**
	 * Returns the Kafka Consumer Group ID 2
	 * @return
	 */
	public String getKafkaConsumerGroup2() {
		return kafkaConsumerGroup2;
	}

	/**
     * Returns Kafka Topic 1
	 * @return
	 */
	public String getKafkaTopic1() {
		return kafkaTopic1;
	}

	/**
     * Returns the No. of Partitions for Topic 1
	 * @return
	 */
	public int getKafkaTopic1Partitions() {
		return kafkaTopic1Partitions;
	}

	/**
     * Returns the No. of Replicas for Topic 1
	 * @return
	 */
	public short getKafkaTopic1Replica() {
		return kafkaTopic1Replica;
	}

	/**
	 * Returns the Ack Type for Topic 1
	 * Ack Types = All, 1, 0
	 * All = Acknowledgement By All the Replicas
	 * 1 = Acknowledgement By the Leader
	 * 0 = Acknowledgement By None
	 *
	 * @return
	 */
	public String getKafkaTopic1AckType() {
		return kafkaTopic1AckType;
	}

	/**
	 * Returns Kafka Topic 2
	 * @return
	 */
	public String getKafkaTopic2() {
		return kafkaTopic2;
	}

	/**
	 * Returns the No. of Partitions for Topic 2
	 * @return
	 */
	public int getKafkaTopic2Partitions() {
		return kafkaTopic2Partitions;
	}

	/**
	 * Returns the No. of Replicas for Topic 2
	 * @return
	 */
	public short getKafkaTopic2Replica() {
		return kafkaTopic2Replica;
	}

	/**
	 * Returns the Ack Type for Topic 1
	 * Ack Types = All, 1, 0
	 * All = Acknowledgement By All the Replicas
	 * 1 = Acknowledgement By the Leader
	 * 0 = Acknowledgement By None
	 *
	 * @return
	 */
	public String getKafkaTopic2AckType() {
		return kafkaTopic2AckType;
	}

	/**
	 * Returns Spring App Name
	 * @return
	 */
	public String getSpringAppName() {
		return springAppName;
	}

	public boolean isCreateKafkaTopic1() {
		return createKafkaTopic1;
	}

	public boolean isCreateKafkaTopic2() {
		return createKafkaTopic2;
	}
}
