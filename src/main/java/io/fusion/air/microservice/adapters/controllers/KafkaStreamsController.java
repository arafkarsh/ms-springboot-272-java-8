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

import io.fusion.air.microservice.adapters.cdc.KafkaCDCPostgreSQLService;
import io.fusion.air.microservice.adapters.messaging.streams.ProductStreamProcessManager;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.HashMap;

/**
 * Kafka Streams Processing Controller
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/kafka/streams")
@RequestScope
@Tag(name = "Kafka Streams Controller", description = "Stream Processing (io.f.a.m.adapters.controllers.KafkaStreamsController)")
public class KafkaStreamsController extends AbstractController {

    @Autowired
    private ProductStreamProcessManager productStreamProcessManager;

    @Operation(summary = "Start Product Transformer - Stream Processing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product Transformer Stream Processing Started!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to start Product Transformer Stream Processing!",
                    content = @Content)
    })
    @PostMapping(path = "/start")
    public ResponseEntity<StandardResponse> startProductStream() throws IOException {
        productStreamProcessManager.startKafkaStreams();
        StandardResponse stdResponse = createSuccessResponse("Product Transformer Stream Processing Started.");
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Stop Product Transformer - Stream Processing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product Transformer Stream Processing Stopped!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to stop Product Transformer Stream Processing!",
                    content = @Content)
    })
    @PostMapping(path = "/stop")
    public ResponseEntity<StandardResponse> stopProductStream() throws IOException {
        productStreamProcessManager.stopKafkaStreams();
        StandardResponse stdResponse = createSuccessResponse("Product Transformer Stream Processing Stopped.");
        return ResponseEntity.ok(stdResponse);
    }
}
