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

import io.fusion.air.microservice.adapters.events.pubsub.ConsumerTopic1;
import io.fusion.air.microservice.adapters.events.pubsub.ConsumerTopic2;
import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
@RequestMapping("${service.api.path}/kafka/sse/")
@RequestScope
@Tag(name = "Kafka SSE Controller", description = "To Stream Events from topics. (io.f.a.m.adapters.controllers.KafkaSSEController)")
public class KafkaSSEController extends AbstractController {

    private final ConsumerTopic1 consumerTopic1;
    private final ConsumerTopic2 consumerTopic2;

    @Autowired
    public KafkaSSEController(ConsumerTopic1 consumer1, ConsumerTopic2 consumer2) {
        this.consumerTopic1 = consumer1;
        this.consumerTopic2 = consumer2;
    }

    @Operation(summary = "Start the Kafka Stream Listener for Topic 1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Stream Listener for Topic 1 started!",
                    content = {@Content(mediaType = "text/event-stream")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Stream Listener for Topic 1 Failed to start!",
                    content = @Content)
    })
    @GetMapping(path = "/topic1/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream1() throws IOException {
        SseEmitter emitter = new SseEmitter();

        // When the client disconnects, remove the emitter
        emitter.onCompletion(() -> consumerTopic1.removeEmitter(emitter));

        // When the client times out, remove the emitter
        emitter.onTimeout(() -> consumerTopic1.removeEmitter(emitter));

        // If there's an error with the connection, remove the emitter
        emitter.onError((e) -> consumerTopic1.removeEmitter(emitter));

        // Optionally, you can set a timeout for how long the connection should be kept open.
        // This will automatically complete the connection after the specified time.
        // Uncomment the following line to set a timeout of 30 minutes.
        // emitter.setTimeout(30 * 60 * 1000L);

        consumerTopic1.addEmitter(emitter);
        return emitter;
    }

    @Operation(summary = "Start the Kafka Stream Listener for Topic 2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Kafka Stream Listener for Topic 2 started!",
                    content = {@Content(mediaType = "text/event-stream")}),
            @ApiResponse(responseCode = "400",
                    description = "Kafka Stream Listener for Topic 2 Failed to start!",
                    content = @Content)
    })
    @GetMapping(path = "/topic2/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream2() throws IOException {
        SseEmitter emitter = new SseEmitter();

        // When the client disconnects, remove the emitter
        emitter.onCompletion(() -> consumerTopic2.removeEmitter(emitter));

        // When the client times out, remove the emitter
        emitter.onTimeout(() -> consumerTopic2.removeEmitter(emitter));

        // If there's an error with the connection, remove the emitter
        emitter.onError((e) -> consumerTopic2.removeEmitter(emitter));

        consumerTopic2.addEmitter(emitter);

        return emitter;
    }

}
