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

## 2. Kafka Setup 

Install the latest version of Kafka. This version is tested with Kafka 3.7.0

1. change the envkafka.sh in the kafka-scripts folder to set the Kafka Path

### 2.1 Start Kafka

1. start Zookeeper Server $ kafka-scripts/start-zc.sh
2. start Kafka Broker $ kafka-scripts/start-ka.sh
3. start Kafka Connect $ kafka-scripts/start-kc.sh

### 2.2 Start the Server 

1. run (to test with H2 In-Memory Database)
2. run prod (to test with PostgreSQL Database) Check the application-prod.properties for PostgreSQL Config

### 2.3 Testing Kafka Examples
<todo>

## 3. State Machine Example
<todo>

## 4. CRUD Examples

Check the <a href="https://github.com/arafkarsh/ms-springboot-272-java-8/blob/main/CRUD_Examples.md">CRUD_Examples.md</a>

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
