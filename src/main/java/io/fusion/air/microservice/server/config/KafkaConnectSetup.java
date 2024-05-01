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

import io.fusion.air.microservice.domain.exceptions.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CDC - Change Data Capture
 * Kafka Connect Example
 *
 * For PostgreSQL
 *
 * 1. Download the Driver for Kafka Connect
 *    https://debezium.io/documentation/reference/stable/install.html
 *
 * 2. config/connect-distributed.properties (Where Kafka is installed)
 *    Add plugin.path=/kafka/connect
 *
 * 3. Update postgresql.conf with the following params
 *    ############ REPLICATION ##############
 *    wal_level = logical
 *    max_wal_senders = 4
 *    max_replication_slots = 4
 *
 * 4. Update pg_hba.conf with the following (if not already present)
 *    ############ REPLICATION ##############
 *    local   replication     postgres                          trust
 *    host    replication     postgres  127.0.0.1/32            trust
 *    host    replication     postgres  ::1/128                 trust
 *
 * 5. CREATE ROLE
 *    CREATE ROLE name REPLICATION LOGIN;
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaConnectSetup {

    @Autowired
    private KafkaConnectConfig kafkaConnect;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * List All the Connectors
     * @return
     */
    public String[] listAllConnectors() {
        // String uri = "http://localhost:8083/connectors";
        String[] connectors = restTemplate.getForObject(kafkaConnect.getConnectUrl(), String[].class);
        System.out.println(Arrays.toString(connectors));
        return connectors;
    }


    /**
     * Delete the Connector
     * @param _connectorName
     * @return
     */
    public String deleteConnector(String _connectorName) {
        // String uri = "http://localhost:8083/connectors/"+_connectorName;
        String uri = kafkaConnect.getConnectUrl() + _connectorName;
        String status = "Connector ("+_connectorName+") deleted successfully";
        try {
            restTemplate.delete(uri);
            System.out.println(status);
            return status;
        } catch (HttpClientErrorException | ResourceAccessException e) {
            status = "Error deleting connector ("+_connectorName+"): " + e.getMessage();
            System.out.println(status);
            throw new MessagingException(status);
        }
    }

    /**
     * Create the PostgreSQL Connector
     *
     * How Debezium Connects to PostgreSQL
     *
     * 1. Logical Replication Setup:
     * Debezium leverages PostgreSQL's logical replication feature, which allows changes to the
     * database (DML statements) to be streamed in real-time to external systems. Logical
     * replication works by decoding the write-ahead log (WAL) of PostgreSQL, which records
     * all changes to the database's data.
     *
     * 2. Replication Slot:
     * Debezium establishes a replication slot on PostgreSQL, which is a stable store for WAL
     * changes that the database maintains until the consuming application (Debezium) confirms
     * their processing. This ensures that all changes can be captured without loss, even if the
     * consuming application temporarily disconnects.
     *
     * 3. Snapshot (Initial Sync):
     * On first run, Debezium can perform an initial snapshot of the entire database (or specific
     * tables), exporting existing records before it starts streaming changes. This is useful for
     * initializing the Kafka topics with the current state of the database.
     *
     * Components Installed on PostgreSQL Server
     * To enable Debezium to capture changes from PostgreSQL (running on Machine C), you need
     * to configure several components:
     *
     * 1. PostgreSQL Configuration Changes:
     * - wal_level set to logical to enable logical decoding.
     * - max_wal_senders and max_replication_slots set to appropriate values to handle connections
     *   and replication slots.
     *
     * 2. Replication User:
     * - A dedicated database user with replication privileges.
     *
     * 3. Publication (For PostgreSQL 10+):
     * Publications aren't strictly necessary for Debezium but are part of PostgreSQL's logical
     * replication capabilities, allowing control over which tables are replicated
     *
     * How Inserts, Updates, and Deletes Are Handled
     *
     * 1. Capture of Changes:
     * As changes occur in the PostgreSQL database:
     * - Inserts generate new WAL entries, which are then converted into Create events.
     * - Updates are converted into Update events, capturing the old and new values (depending on the configuration).
     * - Deletes produce Delete events, with options to include the deleted data in the event payload.
     *
     * 2. Streaming to Kafka:
     * - Debezium reads these events from the replication slot and sends them to Kafka Connect,
     *    which in turn publishes them onto Kafka topics (running on Machine B). Each type of change
     *    (insert, update, delete) can be configured to go to the same or different topics based on the setup.
     *
     * 3. Serialization:
     * - The events are serialized (commonly using JSON or Avro formats) and include metadata such
     *    as the source database, table, and timestamp, along with the actual data change.
     * @return
     */
    public String createPostgresConnectorForProduct() {
        Map<String, Object> config = new HashMap<>();
        config.put("connector.class", kafkaConnect.getConnectClass());                      // Debezium Connector Class
        config.put("database.hostname", kafkaConnect.getConnectDBHost());               // replace with your DB hostname
        config.put("database.port", kafkaConnect.getConnectDBPort()+"");                 // replace with your DB port
        config.put("database.user", kafkaConnect.getConnectDBUser());                    // replace with your DB user
        config.put("database.password", kafkaConnect.getConnectDBPassword());         // replace with your DB password
        config.put("database.dbname", kafkaConnect.getConnectDBName());               // replace with your DB name
        config.put("database.server.name", kafkaConnect.getConnectDBServerName());
        config.put("table.include.list", kafkaConnect.getConnectDBTableList());
        config.put("topic.creation.default.replication.factor", kafkaConnect.getConnectTopicReplica() + "");
        config.put("topic.creation.default.partitions", kafkaConnect.getConnectTopicPartition() + "");
        config.put("slot.name", kafkaConnect.getConnectSlotName());
        config.put("plugin.name", "pgoutput");
        config.put("snapshot.mode", "initial");
        config.put("decimal.handling.mode", "string");
        config.put("topic.prefix", kafkaConnect.getConnectTopicPrefix());                  // adding the topic.prefix value
        config.put("transforms", "unwrap");
        config.put("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState");
        config.put("transforms.unwrap.drop.tombstones", "false");

        Map<String, Object> connectorPayload = new HashMap<>();
        connectorPayload.put("name", "ms-272-products");
        connectorPayload.put("config", config);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(connectorPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                kafkaConnect.getConnectUrl(), HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Connector created successfully");
            return response.getStatusCode() + " Connector created successfully";
        }
        System.out.println("Failed to create connector: " + response.getBody());
        return "Failed to create connector: " + response.getBody();
    }

    /**
    public String createPostgresConnectorForProductOld() {
        String kafkaConnectUri = "http://localhost:8083/connectors"; // replace with your Kafka Connect URI
        Map<String, Object> config = new HashMap<>();
        config.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        config.put("database.hostname", "localhost");               // replace with your DB hostname
        config.put("database.port", "5433");                        // replace with your DB port
        config.put("database.user", "postgres");                    // replace with your DB user
        config.put("database.password", "password");              // replace with your DB password
        config.put("database.dbname", "ms_cache_272");              // replace with your DB name
        config.put("database.server.name", "ms_cache");
        config.put("table.include.list", "ms_schema.products_m");
        config.put("topic.creation.default.replication.factor", "1");
        config.put("topic.creation.default.partitions", "1");
        config.put("slot.name", "ms_272_slot");
        config.put("plugin.name", "pgoutput");
        config.put("snapshot.mode", "initial");
        config.put("decimal.handling.mode", "string");
        config.put("topic.prefix", "ms_cache_272");                  // adding the topic.prefix value
        config.put("transforms", "unwrap");
        config.put("transforms.unwrap.type", "io.debezium.transforms.ExtractNewRecordState");
        config.put("transforms.unwrap.drop.tombstones", "false");

        Map<String, Object> connectorPayload = new HashMap<>();
        connectorPayload.put("name", "ms-272-products");
        connectorPayload.put("config", config);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(connectorPayload, headers);

        ResponseEntity<String> response = restTemplate.exchange(kafkaConnectUri, HttpMethod.POST, entity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Connector created successfully");
            return response.getStatusCode() + " Connector created successfully";
        }
        System.out.println("Failed to create connector: " + response.getBody());
        return "Failed to create connector: " + response.getBody();
    }
     */
}
