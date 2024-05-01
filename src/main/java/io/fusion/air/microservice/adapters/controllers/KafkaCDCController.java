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

import io.fusion.air.microservice.server.config.KafkaConnectSetup;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

/**
 * Kafka SSE (Server Sent Event) Controller
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/kafka/cdc")
@RequestScope
@Tag(name = "Kafka CDC Controller", description = "Connect DB & Stream data to Topics (io.f.a.m.adapters.controllers.KafkaCDCController)")
public class KafkaCDCController extends AbstractController {

    @Autowired
    private KafkaConnectSetup kafkaDBConnectService;


    @Operation(summary = "List All Connectors Registered with Kafka Connect")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retrieved All Connectors registered!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to retrieve all the Connectors",
                    content = @Content)
    })
    @GetMapping(path = "/")
    public ResponseEntity<StandardResponse> getAllConnectors() throws IOException {
        String[] response = kafkaDBConnectService.listAllConnectors();
        StandardResponse stdResponse = null;
        if(response != null && response.length > 0) {
            stdResponse= createSuccessResponse("Kafka Connector List Retrieved = "+response.length);
            stdResponse.setPayload(response);
        } else {
            stdResponse= createSuccessResponse("No Connectors Found!");

        }
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Delete Connector Registered with Kafka Connect")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Deleted the registered Connector!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to delete the registered Connector",
                    content = @Content)
    })
    @DeleteMapping(path = "/{connectorName}")
    public ResponseEntity<StandardResponse> deleteConnector(@PathVariable("connectorName") String _connectorName) throws IOException {
        String response = kafkaDBConnectService.deleteConnector(_connectorName);
        StandardResponse stdResponse = createSuccessResponse("Kafka Connector deleted.");
        stdResponse.setPayload(response);
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Setup PostgreSQL for Streaming Product data to Topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product Table setup for Streaming!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to setup Product Table for Streaming",
                    content = @Content)
    })
    @PostMapping(path = "/product")
    public ResponseEntity<StandardResponse> dbProductSetup() throws IOException {
        String response = kafkaDBConnectService.createPostgresConnectorForProduct();
        StandardResponse stdResponse = createSuccessResponse("PostgreSQL Configured for Product Table");
        stdResponse.setPayload(response);
        return ResponseEntity.ok(stdResponse);
    }
}
