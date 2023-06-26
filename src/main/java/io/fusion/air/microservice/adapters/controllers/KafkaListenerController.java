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
package io.fusion.air.microservice.adapters.controllers;

import io.fusion.air.microservice.adapters.events.core.KafkaConsumerService;
import io.fusion.air.microservice.adapters.events.core.KafkaMemberDTO;
import io.fusion.air.microservice.adapters.events.core.KafkaPartitionManager;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/kafka")
@RequestScope
@Tag(name = "Kafka Listener Controller", description = "To Manage Partitions & Start/Stop Listeners (io.f.a.m.adapters.controllers.KafkaListenerController)")
public class KafkaListenerController extends AbstractController {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    private final KafkaListenerEndpointRegistry registry;

    private final KafkaPartitionManager partitionManager;

    @Autowired
    public KafkaListenerController(KafkaListenerEndpointRegistry _registry, KafkaPartitionManager _partitionManager) {
        this.registry = _registry;
        this.partitionManager = _partitionManager;
    }

    @Operation(summary = "Start the Kafka Fusion Listener for a Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for a Topic started!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for a Topic Failed to start!",
                    content = @Content)
    })
    @GetMapping("/consumers/topic/{topic}")
    public ResponseEntity<StandardResponse> getConsumers(@PathVariable("topic") String _topic) {
        Map<String, List<KafkaMemberDTO>> consumers = kafkaConsumerService.getConsumersAcrossGroups(_topic);
        StandardResponse stdResponse = createSuccessResponse("Consumers List Fetched! = "+consumers.size());
        stdResponse.setPayload(consumers);
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Start the Kafka Fusion Listener for ALL Topics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for ALL Topic started!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for ALL Topic Failed to start!",
                    content = @Content)
    })
    @PostMapping("/start/all")
    public ResponseEntity<StandardResponse> start() {
        registry.getListenerContainer("fusionListenerT1").start();
        registry.getListenerContainer("fusionListenerT2").start();
        StandardResponse stdResponse = createSuccessResponse("Listeners Started!");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("fusionListenerT1", "Started");
        data.put("fusionListenerT2", "Started");
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);

    }

    @Operation(summary = "Stop the Kafka Fusion Listener for ALL Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for ALL Topic stopped!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for ALL Topic Failed to stop!",
                    content = @Content)
    })
    @PostMapping("/stop/all")
    public ResponseEntity<StandardResponse>  stop() {
        registry.getListenerContainer("fusionListenerT1").stop();
        registry.getListenerContainer("fusionListenerT2").stop();
        StandardResponse stdResponse = createSuccessResponse("Listeners Stopped!");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("fusionListenerT1", "Stopped");
        data.put("fusionListenerT2", "Stopped");
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Start the Kafka Fusion Listener for Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for Topic started!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for Topic Failed to start!",
                    content = @Content)
    })
    @PostMapping("/start/{listenerId}")
    public ResponseEntity<StandardResponse>  start(@PathVariable("listenerId") String _listenerId) {
        registry.getListenerContainer(_listenerId).start();
        StandardResponse stdResponse = createSuccessResponse("Listeners Started!");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(_listenerId, "Started");
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Stop the Kafka Fusion Listener for Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for Topic stopped!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for Topic Failed to stop!",
                    content = @Content)
    })
    @PostMapping("/stop/{listenerId}")
    public ResponseEntity<StandardResponse>  stop(@PathVariable("listenerId") String _listenerId) {
        registry.getListenerContainer(_listenerId).stop();
        StandardResponse stdResponse = createSuccessResponse("Listeners Stopped!");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(_listenerId, "Stopped");
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Update the Partitions for a Kafka Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for Topic 1 stopped!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for Topic 1 Failed to stop!",
                    content = @Content)
    })
    @PutMapping("/topic/{topicName}/partitions/{partitionNumber}")
    public ResponseEntity<StandardResponse>  updatePartitions(
            @PathVariable("topicName") String _topicName,
            @PathVariable("partitionNumber") int _partitionNumber) {

        partitionManager.increasePartitions(_topicName, _partitionNumber);
        StandardResponse stdResponse = createSuccessResponse("Topic ="+_topicName
                                                +" Partitions = "+_partitionNumber+" Updated!");
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("Topic", _topicName);
        data.put("Partitions", ""+_partitionNumber);
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);
    }

}
