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

import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/kafka/topic1")
@RequestScope
@Tag(name = "Kafka Listener Controller", description = "Ex. io.f.a.m.adapters.controllers.KafkaRestController")
public class KafkaRestController extends AbstractController {

    private final KafkaListenerEndpointRegistry registry;

    @Autowired
    public KafkaRestController(KafkaListenerEndpointRegistry registry) {
        this.registry = registry;
    }

    @Operation(summary = "Start the Kafka Fusion Listener for Topic 1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for Topic 1 started!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for Topic 1 Failed to start!",
                    content = @Content)
    })
    @PostMapping("/start")
    public void start() {
        registry.getListenerContainer("fusionListenerT1").start();
    }

    @Operation(summary = "Stop the Kafka Fusion Listener for Topic 1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Listener for Topic 1 stopped!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Listener for Topic 1 Failed to stop!",
                    content = @Content)
    })
    @PostMapping("/stop")
    public void stop() {
        registry.getListenerContainer("fusionListenerT1").stop();
    }

}
