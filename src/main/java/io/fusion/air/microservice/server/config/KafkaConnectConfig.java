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
 * Kafka Connect Configuration
 *
 * @author arafkarsh
 *
 */
@Component
@Configuration
@EnableKafka
@PropertySource(
		name = "kafkaConnectConfig",
		// Expects file in the directory the jar is executed
		value = "file:./application.properties")
		// Expects the file in src/main/resources folder
		// value = "classpath:application.properties")
		// value = "classpath:application2.properties,file:./application.properties")
public class KafkaConnectConfig extends KafkaPubSubConfig implements Serializable {

	@JsonIgnore
	// KAFKA Connect URL
	@Value("${kafka.connect.url:http://localhost:8083/connectors}")
	private String connectUrl;

	@JsonIgnore
	// Kafka Connector Class Name
	@Value("${kafka.connect.clas:io.debezium.connector.postgresql.PostgresConnector}")
	private String connectClass;

	@JsonIgnore
	// Kafka Connect DB Host
	@Value("${kafka.connect.db.host:localhost}")
	private String connectDBHost;

	@JsonIgnore
	// Kafka Connect DB Port
	@Value("${kafka.connect.db.port:5433}")
	private int connectDBPort;

	@JsonIgnore
	// Kafka Connect DB User Name
	@Value("${kafka.connect.db.user:postgres}")
	private String connectDBUser;

	@JsonIgnore
	// Kafka Connect DB Password Name
	@Value("${kafka.connect.db.password:demo}")
	private String connectDBPassword;

	@JsonIgnore
	// Kafka Connect DB Name
	@Value("${kafka.connect.db.name:ms_cache_272}")
	private String connectDBName;

	@JsonIgnore
	// Kafka Connect DB Server Name
	@Value("${kafka.connect.db.server.name:ms_cache}")
	private String connectDBServerName;

	@JsonIgnore
	// Kafka Connect DB Table List
	@Value("${kafka.connect.table.include.list:ms_schema.products_m}")
	private String connectDBTableList;

	@JsonIgnore
	// Kafka Connect Topic Replica
	@Value("${kafka.connect.topic.replica:1}")
	private int connectTopicReplica;

	@JsonIgnore
	// Kafka Connect Topic Partition
	@Value("${kafka.connect.topic.partition:1}")
	private int connectTopicPartition;

	@JsonIgnore
	// Kafka Connect Topic Prefix
	@Value("${kafka.connect.topic.prefix:ms_cache_272}")
	private String connectTopicPrefix;

	@JsonIgnore
	// Kafka Connect Slot Name
	@Value("${kafka.connect.slot.name:ms_272_slot}")
	private String connectSlotName;

	public String getConnectUrl() {
		return connectUrl;
	}

	public String getConnectClass() {
		return connectClass;
	}

	public String getConnectDBHost() {
		return connectDBHost;
	}

	public int getConnectDBPort() {
		return connectDBPort;
	}

	public String getConnectDBUser() {
		return connectDBUser;
	}

	public String getConnectDBPassword() {
		return connectDBPassword;
	}

	public String getConnectDBName() {
		return connectDBName;
	}

	public String getConnectDBServerName() {
		return connectDBServerName;
	}

	public String getConnectDBTableList() {
		return connectDBTableList;
	}

	public int getConnectTopicReplica() {
		return connectTopicReplica;
	}

	public int getConnectTopicPartition() {
		return connectTopicPartition;
	}

	public String getConnectTopicPrefix() {
		return connectTopicPrefix;
	}

	public String getConnectSlotName() {
		return connectSlotName;
	}
}
