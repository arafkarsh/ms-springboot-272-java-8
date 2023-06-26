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
public class KafkaStreamsConfig extends KafkaConfig implements Serializable {

	// KAFKA Streams Configurations
	@Value("${spring.kafka.streams.auto-startup:true}")
	private boolean streamsAutoStart;

	@JsonIgnore
	@Value("${kafka.streams.input.topic:inputTopic}")
	private String streamInputTopic;

	@JsonIgnore
	@Value("${kafka.streams.output.topic:outputTopic}")
	private String streamOutputTopic;

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
	public String getStreamInputTopic() {
		return streamInputTopic;
	}

	/**
	 * Streams Output Topic
	 * @return
	 */
	public String getStreamOutputTopic() {
		return streamOutputTopic;
	}
}
