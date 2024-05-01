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
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Kafka Streams Configuration
 *
 * @author arafkarsh
 *
 */
@Component
@Configuration
@EnableKafka
@PropertySource(
		name = "kafkaStreamsConfig",
		// Expects file in the directory the jar is executed
		value = "file:./application.properties")
		// Expects the file in src/main/resources folder
		// value = "classpath:application.properties")
		// value = "classpath:application2.properties,file:./application.properties")
public class KafkaStreamsConfig extends KafkaPubSubConfig implements Serializable {

	// KAFKA Streams Configurations
	@Value("${spring.kafka.streams.auto-startup:true}")
	private boolean streamsAutoStart;

	@JsonIgnore
	// Create Kafka Streams Topic 1
	@Value("${kafka.streams.topic.1.create:false}")
	private boolean createKafkaStreamsTopic1;

	@JsonIgnore
	@Value("${kafka.streams.topic.1:topic1}")
	private String streamTopic1;

	@JsonIgnore
	// Kafka Streams Topic 1 and its Partitions
	@Value("${kafka.streams.topic.1.partitions:1}")
	private int kafkaStreamsTopic1Partitions;

	@JsonIgnore
	// Kafka Streams Topic 1 and its Replicas
	@Value("${kafka.streams.topic.1.replica:1}")
	private short kafkaStreamsTopic1Replica;

	@JsonIgnore
	// Create Kafka Streams Topic 2
	@Value("${kafka.streams.topic.2.create:false}")
	private boolean createKafkaStreamsTopic2;

	@JsonIgnore
	@Value("${kafka.streams.topic.2:topic2}")
	private String streamTopic2;

	@JsonIgnore
	// Kafka Streams Topic 2 and its Partitions
	@Value("${kafka.streams.topic.2.partitions:1}")
	private int kafkaStreamsTopic2Partitions;

	@JsonIgnore
	// Kafka Streams Topic 2 and its Replicas
	@Value("${kafka.streams.topic.2.replica:1}")
	private short kafkaStreamsTopic2Replica;

	@JsonIgnore
	// Create Kafka Streams Topic 3
	@Value("${kafka.streams.topic.3.create:false}")
	private boolean createKafkaStreamsTopic3;

	@JsonIgnore
	@Value("${kafka.streams.topic.3:topic3}")
	private String streamTopic3;

	@JsonIgnore
	// Kafka Streams Topic 3 and its Partitions
	@Value("${kafka.streams.topic.3.partitions:1}")
	private int kafkaStreamsTopic3Partitions;

	@JsonIgnore
	// Kafka Streams Topic 3 and its Replicas
	@Value("${kafka.streams.topic.3.replica:1}")
	private short kafkaStreamsTopic3Replica;

	/**
	 * Returns True if Auto Start is enabled
	 * @return
	 */
	public boolean isStreamsAutoStart() {
		return streamsAutoStart;
	}

	/**
	 * Streams Input Topic
	 * @return
	 */
	public String getStreamTopic1() {
		return streamTopic1;
	}

	/**
	 * Streams Output Topic
	 * @return
	 */
	public String getStreamTopic2() {
		return streamTopic2;
	}

	public String getStreamTopic3() {
		return streamTopic3;
	}

	public int getKafkaStreamsTopic1Partitions() {
		return kafkaStreamsTopic1Partitions;
	}

	public short getKafkaStreamsTopic1Replica() {
		return kafkaStreamsTopic1Replica;
	}

	public int getKafkaStreamsTopic2Partitions() {
		return kafkaStreamsTopic2Partitions;
	}

	public short getKafkaStreamsTopic2Replica() {
		return kafkaStreamsTopic2Replica;
	}

	public int getKafkaStreamsTopic3Partitions() {
		return kafkaStreamsTopic3Partitions;
	}

	public short getKafkaStreamsTopic3Replica() {
		return kafkaStreamsTopic3Replica;
	}

	public boolean isCreateKafkaStreamsTopic1() {
		return createKafkaStreamsTopic1;
	}

	public boolean isCreateKafkaStreamsTopic2() {
		return createKafkaStreamsTopic2;
	}

	public boolean isCreateKafkaStreamsTopic3() {
		return createKafkaStreamsTopic3;
	}
}
