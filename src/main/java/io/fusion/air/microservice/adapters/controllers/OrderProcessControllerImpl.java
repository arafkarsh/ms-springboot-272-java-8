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
// Custom
import io.fusion.air.microservice.domain.entities.order.OrderEntity;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.domain.ports.services.OrderService;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.controllers.AbstractController;
// Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
// Java & Utils
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order Process
 *
 * @author arafkarsh
 * @version 1.0
 * 
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/order/process")
@RequestScope
@Tag(name = "Order Process API", description = "To Manage (Credits/Payments/Package/Shipment/Delivery/Cancellation) of Order.")
public class OrderProcessControllerImpl extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());
	
	@Autowired
	private ServiceConfiguration serviceConfig;
	private String serviceName;

	@Autowired
	private OrderService orderService;

	/**
	 * GET Method Call to Get Order for the Customer
	 * 
	 * @return
	 */
    @Operation(summary = "Get The Order for the Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Order Retrieved!",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
            description = "Invalid Order ID",
            content = @Content)
    })
	@GetMapping("/customer/{customerId}")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchOrder(@PathVariable("customerId") String customerId) throws Exception {
		log.debug("|"+name()+"|Request to Get Order For the Customer "+customerId);
		List<OrderEntity> orders = orderService.findByCustomerId(customerId);
		StandardResponse stdResponse = createSuccessResponse("Order Retrieved. Orders =  "+orders.size());
		stdResponse.setPayload(orders);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Get the Order by Customer ID and Order ID
	 * @param customerId
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	@Operation(summary = "Get The Order for the Customer by Order ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Order Retrieved!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Order ID",
					content = @Content)
	})
	@GetMapping("/customer/{customerId}/order/{orderId}")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchOrder(
			@PathVariable("customerId") String customerId, @PathVariable("orderId") String orderId) throws Exception {
		log.debug("|"+name()+"|Request to Get Order For the Customer "+customerId);
		Optional<OrderEntity> order = orderService.findById(customerId, orderId);
		StandardResponse stdResponse = createSuccessResponse("Order Retrieved!");
		stdResponse.setPayload(order.get());
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Order: Initiate Credit Approval
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	@Operation(summary = "State Machine Demo 1 - Credit Approval")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Order Send for Credit Approval!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Send Order for Credit Approval!",
					content = @Content)
	})
	@PostMapping("/credit/customer/{customerId}/order/{orderId}")
	public ResponseEntity<StandardResponse> creditCheckingRequest(
			@PathVariable("customerId") String customerId, @PathVariable("orderId") String orderId) {
		log.debug("|"+name()+"|Request to Credit Approval Order ID ... "+orderId);
		OrderEntity order = orderService.processCreditApproval(customerId, orderId);
		StandardResponse stdResponse = createSuccessResponse("Order Send for Credit Approval Request!");
		stdResponse.setPayload(order);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Event Handling for Testing Purpose ONLY
	 * @param event
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	@Operation(summary = "State Machine Demo 2 - Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Order updated with Event!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to update Order for the Event!",
					content = @Content)
	})
	@PostMapping("/event/{event}/customer/{customerId}/order/{orderId}")
	public ResponseEntity<StandardResponse> eventHandlingRequest(@PathVariable("event") String event,
			@PathVariable("customerId") String customerId, @PathVariable("orderId") String orderId) {
		log.debug("|"+name()+"|Event Handling for Order ID ... "+orderId);
		OrderEntity order = orderService.handleEvent(customerId, orderId, event);
		StandardResponse stdResponse = createSuccessResponse("Order Event = "+event);
		stdResponse.setPayload(order);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Order: RESET Order State
	 * ONLY FOR TESTING THE STATE MACHINE
	 *
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	@Operation(summary = "State Machine Demo 0 - RESET ORDER STATE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Order State Reset!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Reset Order State",
					content = @Content)
	})
	@PostMapping("/reset/customer/{customerId}/order/{orderId}")
	public ResponseEntity<StandardResponse> resetOrder(
			@PathVariable("customerId") String customerId, @PathVariable("orderId") String orderId) {
		log.debug("|"+name()+"|Request to Reset Order State. Order ID ... "+orderId);
		OrderEntity order = orderService.resetOrder(customerId, orderId);
		StandardResponse stdResponse = createSuccessResponse("Order State Reset!");
		stdResponse.setPayload(order);
		return ResponseEntity.ok(stdResponse);
	}

 }