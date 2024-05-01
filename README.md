# Microservice Cache / Kafka Template

## Microservice Structure

![Package Structure](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/MS-Pkg-Structure.jpg)

### What the Template Provides out of the box

1. Springboot App with Swagger Docs (...adapters.controllers)
2. Exception Handling with Exception Framework using AOP ( ..adapters.aop)
3. Log Management using Logback  (...adapters.filters)
4. Standardized REST Responses (...domain.models.StandardResponse) 
5. Security using JWT Tokens (...adapters.security)
6. Encrypting Sensitive Data using Encryption Algorithms (...security)
7. JPA configurations for H2 and PostgreSQL (...server.config)
8. Kafka Examples

### Pre-Requisites 

1. Springboot 2.7.2
2. Java 8, Java 17 (To run)
3. Kafka 3.x (3.7.0)
3. Java EE (javax.servlet.*, javax.persistence.*, javax.validation.*)
4. Maven 3.8.6
5. Git 2.31

### Microservice Cache / Kafka Template gives you a 

1. SpringBoot App template with 
2. Kafka Pub/Sub Examples
3. Kafka Connect Examples
4. Kafka Streams Examples
5. Kafka Global Table Example 
6. Spring State Machine Examples (Reservation API)
7. Spring Pagination Example (Country API)
8. CRUD Examples (Product API)
9. SOLID Design Pattern Examples 
10. JWT Tokens Example (Secured Payments API )
11. Spring Profiles works with H2 DB on Dev and PostgreSQL in Staging / Prod mode 
12. H2 In Memory Database & PostgreSQL Database Support 
13. Open API 3 Ex, 
14. Spring Actuator, 
15. Spring Sleuth and 
16. POM File with (SpringBoot) Fat and Thin (Maven) jar file creation and 
17. Dockerfile for containerisation.

## 1. Setting up the Template

### Step 1.1 - Getting Started

1. git clone https://github.com/arafkarsh/ms-springboot-272-java-8.git
2. cd ms-springboot-272-java-8

###  Step 1.2 - Compile (Once your code is ready) 

#### 1.2.1 Compile the Code
Run the "compile" from ms-springboot-272-java-8
1. compile OR ./compile (Runs in Linux and Mac OS)
2. mvn clean; mvn -e package; (All Platforms)
3. Use the IDE Compile options

#### 1.2.2 What the "Compile" Script will do

1. Clean up the target folder
2. Generate the build no. and build date (takes application.properties backup)
3. build final output SpringBoot fat jar and maven thin jar
4. copy the jar files (and dependencies) to src/docker folder
5. copy the application.properties file to current folder and src/docker folder

In Step 1.2.2 application.properties file will be auto generated by the "compile" script. This is a critical step.
Without generated application.properties file the service will NOT be running. There is pre-built application properties file.

###  Step 1.3 - Run

#### 1.3.1 Start the Service
1. run OR ./run (Runs in Linux or Mac OS, 
2. run prod (to use PostgreSQL DB)
3. mvn spring-boot:run (All Platforms)

#### 1.3.2 Test the Service 
1. test OR ./test (Runs in Linux or Mac OS)
2. Execute the curl commands directly (from the test script)

#### $ run Result 
![Run Results](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/MS-Run-Result.jpg)

#### MS Cache Swagger UI Docs for Testing
![Swagger Docs](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/MS-Cache-Swagger-UI.jpg)

## 2. CRUD Examples

1. Setting up Postman with REST Endpoints for Testing
2. CRUD Examples 
3. JWT Token Examples

Check the <a href="https://github.com/arafkarsh/ms-springboot-272-java-8/blob/main/CRUD_Examples.md">CRUD_Examples.md</a>

## 3. Kafka Setup 

Install the latest version of Kafka. This version is tested with Kafka 3.7.0

1. change the envkafka.sh in the kafka-scripts folder to set the Kafka Path
```
$ cd kafka-scripts
$ vi envkafka.sh OR nano envkafka.sh
```
Change the Kafka Path 
```
#!/bin/sh
KV=-3.7.0
KAFKAPATH=~/Softwares/kafka$KV
echo $KAFKAPATH
```
### Kafka Pub / Sub Configurations 
![Kafka Config PubSub](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Config-PubSub.jpg)

### Kafka Streams Configuration
![Kafka Config Streams](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Config-Streams.jpg)


### 3.1 Start Kafka

1. start Zookeeper Server 
```
$ kafka-scripts/start-zc.sh
```
2. start Kafka Broker 
```
$ kafka-scripts/start-ka.sh
```
3. start Kafka Connect (java 17+)
```
$ kafka-scripts/start-kc.sh
```

### 3.2 Start the Server 

1. run (to test with H2 In-Memory Database)
2. run prod (to test with PostgreSQL Database) Check the application-prod.properties for PostgreSQL Config

### 3.3 Kafka Pub / Sub Examples

#### 3.3.1 Kafka Consumer to handle Topic 1 and Topic 2

| Topic Name   | Partitions | Replica | Acks  | Consumer Group |
|--------------|-----------|---------|------|----------------|
| fusionTopic1 | 1           |1         | 1      | fusionGroup1   |
| fusionTopic2 | 1           |1         | 1      | fusionGroup2   |

Ensure that Kafka Consumers are enabled (autostart = true) For Consumer Topic 1 and Topic 2

![Kafka Consumer](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Listener-1.jpg)

You can programatically Start and Stop the listeners (Consumers) using REST Endpoints (This is for demo purpose ONLY)

![Kafka Listener APIs](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Listener-APIs.jpg)

#### 3.3.2 Kafka Producer to handle Topic 1 and Topic 2

Kafka Producer 1 to send messages to Topic 1 (fusionTopic1)

![Kafka Producer](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Producer-1.jpg)

REST Endpoint to send messages to Topic 1

![Kafka Producer-API](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Producer-APIs.jpg)

#### 3.3.3 Server Side Events using REST Endpoints. 

Demo of Server Side Events using Kafka Listener (Topic 1). Ideally Web Sockets should be used instead of REST Endpoints.
Spring provides the SseEmitter class which is useful for sending SSE events to clients. It can be returned from a controller 
method to handle the stream of server-sent events.

![Kafka SSE](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-SSE-Result.jpg)

### 3.4 Kafka Connect 

#### 3.4.1 Setup Debezium Drivers 

##### Step 1:
PostgreSQL Debezium driver is already available in kafka-connect directory.

To install the debezium drivers for any database, Download the driver from <a href="https://debezium.io/documentation/reference/stable/install.html">debezium site.</a>

1. Install these under plugins directory in the Kafka installation
2. Open the config connect-distributed.properties and add following towards the end of the file
3. plugin.path=/kafka/plugins (The directory you have downloaded the debezium driver)

#### 3.4.2 Kafka Connect Configuration for PostgreSQL Database with Products Table

##### How Debezium Connects to PostgreSQL
1. Logical Replication Setup:
Debezium leverages PostgreSQL's logical replication feature, which allows changes to the database
(DML statements) to be streamed in real-time to external systems. Logical replication works by 
decoding the write-ahead log (WAL) of PostgreSQL, which records all changes to the database's data.

2. Replication Slot:
Debezium establishes a replication slot on PostgreSQL, which is a stable store for WAL changes 
that the database maintains until the consuming application (Debezium) confirms their processing. 
This ensures that all changes can be captured without loss, even if the consuming application 
temporarily disconnects.

4. Snapshot (Initial Sync):
On first run, Debezium can perform an initial snapshot of the entire database (or specific tables), 
exporting existing records before it starts streaming changes. This is useful for initializing the 
Kafka topics with the current state of the database.

##### Components Installed on PostgreSQL Server

To enable Debezium to capture changes from PostgreSQL (running on Machine C), you need to 
configure several components:

1. PostgreSQL Configuration Changes:
- wal_level set to logical to enable logical decoding.
- max_wal_senders and max_replication_slots set to appropriate values to handle connections and replication slots.

3. Replication User:
- A dedicated database user with replication privileges.

3. Publication (For PostgreSQL 10+):
- Publications aren't strictly necessary for Debezium but are part of PostgreSQL's logical replication capabilities, allowing control over which tables are replicated

##### How Inserts, Updates, and Deletes Are Handled

1. Capture of Changes:
As changes occur in the PostgreSQL database:
- Inserts generate new WAL entries, which are then converted into Create events.
- Updates are converted into Update events, capturing the old and new values (depending on the configuration).
- Deletes produce Delete events, with options to include the deleted data in the event payload.

2. Streaming to Kafka:
Debezium reads these events from the replication slot and sends them to Kafka Connect, which in 
turn publishes them onto Kafka topics (running on Machine B). Each type of change (insert, update, 
delete) can be configured to go to the same or different topics based on the setup.

3. Serialization:
The events are serialized (commonly using JSON or Avro formats) and include metadata such as 
the source database, table, and timestamp, along with the actual data change.

##### Step 2:
Modify the PostgreSQL configuration files (postgresql.conf) to enable logical replication:
```
#------------------------------------------------------------------------------
# Kafka Connect Settings
#------------------------------------------------------------------------------
# REPLICATION
wal_level = logical
max_wal_senders = 4
max_replication_slots = 4
```

##### Step 3:
Allow Replication Connections:
Modify the pg_hba.conf file to allow the Debezium user to connect for replication purposes:

```
# TYPE  DATABASE        USER            ADDRESS                 METHOD

# "local" is for Unix domain socket connections only
local   all             all                                     trust
local   all             postgres                                trust
local   all             youruser                               trust

# Allow replication connections from localhost, by a user with the
# replication privilege.
# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   replication     all                                     trust
host    replication     all             127.0.0.1/32            trust
host    replication     all             ::1/128                 trust

```
##### Step 4:

Create Role in the PostgreSQL Database
```
CREATE ROLE name WITH REPLICATION LOGIN PASSWORD 'password';
```

##### Step 5:
Restart the PostgreSQL database. 

#### 3.4.3 Start the Kafka Connect Server

Kafka Connect requires Java 17+

##### Step 6:
To Start the Kafka Connect Server
```
$ kafka-scripts/start-kc.sh
```

#### 3.4.4 Kafka Connect Configurations

Kafka Connect Configuration
![Kafka Connect-Config](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Config-Connect.jpg)

Kafka Connect Setup
![Kafka Connect-Setup](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Connect-API.jpg)

#### 3.4.5 Test the Debezium Driver with Kafka Connect. 

Kafka Connect to PostgreSQL
![Kafka Connect-Create](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Connect-Setup-1.jpg)

Kafka Connect - List the Connectors
![Kafka Connect-Create](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Connect-Setup-2.jpg)

### 3.5 Kafka Streams 

Following Steps involved in Testing the Kafka Connect and Streams.

1. Create a Product Using REST Endpoint and the Record is created in PostgreSQL Database
2. Kafka Connect immediately sends the record to Kafka Topic 
3. Query all the records from Kafka Streams using Global Table
4. Query a Specific Record by UUID of that Record from Kafka Streams Global Table
5. Update the record in Product DB using Update Product REST Endpoint
6. Query the same record again using UUID from Kafka Streams Global Table

1. Create Product (REST Endpoint in  Product API)
![Kafka Streams-Create-1](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Streams-Product-Create.jpg)
![Kafka Streams-Create-2](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Streams-Product-Create-2.jpg)

2. Kafka Connect immediately sends the record to Kafka Topic
3. Query all the records from Kafka Streams using Global Table
![Kafka Streams-Query-1](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Streams-Product-Query-All.jpg)

4. Query a Specific Record by UUID of that Record from Kafka Streams Global Table
![Kafka Streams-Query-2](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Streams-Product-Query.jpg)

5. Update the record in Product DB using Update Product REST Endpoint
![Kafka Streams-Update](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Streams-Product-Update.jpg)

6. Query the same record again using UUID from Kafka Streams Global Table
![Kafka Streams-Update](https://raw.githubusercontent.com/arafkarsh/ms-springboot-272-java-8/master/diagrams/kafka/Kafka-Streams-Product-Query-2.jpg)

## 4. State Machine Example
<todo>

## 5. Docker Container Setup

### Step 5.1 - Verify Container Name and Org Name

1. Verify the Org Name in src/main/resources/app.props.tmpl file (service.org)
2. Verify the container name in src/main/resources/app.props.tmpl file (service.container)
3. Verify the microservice name in src/main/resources/app.props.tmpl file (service.api.name)

### Step 5.2 - Build the image

1. build (Build the Container)
2. scan (Scan the container vulnerabilities)

### Step 5.3 - Test the image

1. start (Start the Container)
2. logs (to view the container logs) - Wait for the Container to Startup
3. Check the URL in a Browser

### Step 5.4 - Push the image to Container Cloud Repository

Update the Org Name in src/main/resources/app.props.tmpl file (service.org)
Setup the Docker Hub or any other Container Registry

1. push (Push the Container to Docker Hub)

### Step 5.5 Other Commands

1. stop (Stop the Container)
2. stats (show container stats)


(C) Copyright 2021-24 : Apache 2 License : Author: Araf Karsh Hamid

<pre> 
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
</pre> 
