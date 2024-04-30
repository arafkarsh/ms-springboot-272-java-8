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

import io.fusion.air.microservice.adapters.events.pubsub.ProducerTopic1;
import io.fusion.air.microservice.adapters.events.pubsub.ProducerTopic2;
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

import java.util.HashMap;

/**
 * Kafka Producer Controller
 *
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
@Tag(name = "Kafka Producer Controller", description = "To Send Messages to Topics (io.f.a.m.adapters.controllers.KafkaProducerController)")
public class KafkaProducerController extends AbstractController {

    @Autowired
    private ProducerTopic1 kafkaProducerTopic1;

    @Autowired
    private ProducerTopic2 kafkaProducerTopic2;


    @Operation(summary = "Send the Message to Kafka Fusion Topic 1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Message Send to Kafka Fusion Topic 1!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to send Message to Kafka Fusion Topic 1!",
                    content = @Content)
    })
    @PostMapping("/topic1/{message}")
    public ResponseEntity<StandardResponse>  sendMessage2Topic1(@PathVariable("message") String _message) {
        kafkaProducerTopic1.sendMessage(_message);
        StandardResponse stdResponse = createSuccessResponse("Message Send to Topic 1");
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("msg", _message);
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);
    }

    @Operation(summary = "Send the Message to Kafka Fusion Topic 2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Message Send to Kafka Fusion Topic 2!",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    description = "Unable to send Message to Kafka Fusion Topic 2!",
                    content = @Content)
    })
    @PostMapping("/topic2/{message}")
    public ResponseEntity<StandardResponse>  sendMessage2Topic2(@PathVariable("message") String _message) {
        kafkaProducerTopic2.sendMessage(_message);
        StandardResponse stdResponse = createSuccessResponse("Message Send to Topic 2");
        HashMap<String,String> data = new HashMap<String, String>();
        data.put("msg", _message);
        stdResponse.setPayload(data);
        return ResponseEntity.ok(stdResponse);
    }
}
