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
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.domain.ports.services.ReservationService;
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
// Java / utils
import org.slf4j.Logger;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation Process
 *
 * @author arafkarsh
 * @version 1.0
 * 
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/reservation")
@RequestScope
@Tag(name = "State Machine Example: Reservation API", description = "To Manage Hotel/Rental/Flight booking.")
public class ReservationProcessControllerImpl extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());
	
	@Autowired
	private ServiceConfiguration serviceConfig;
	private String serviceName;

	@Autowired
	private ReservationService reservationService;

	/**
	 * GET Method Call to ALL Reservation
	 *
	 * @return
	 */
	@Operation(summary = "Get The Reservations")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Reservation Request Retrieved!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Reservation ID",
					content = @Content)
	})
	@GetMapping("/all")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchAllReservations() throws Exception {
		log.debug("|"+name()+"|Request to Get Reservation For the Customers ");
		List<ReservationEntity> reservations = reservationService.findAll();
		StandardResponse stdResponse = createSuccessResponse("Reservation Retrieved. reservations =  "+reservations.size());
		stdResponse.setPayload(reservations);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Save Reservation
	 */
	@Operation(summary = "Save Reservation")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Reservation Saved!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Save Reservation",
					content = @Content)
	})
	@PostMapping("/save")
	public ResponseEntity<StandardResponse> saveReservation(@Valid @RequestBody ReservationEntity _reservation) {
		log.debug("|"+name()+"|Request to Save Reservation ... "+_reservation);
		ReservationEntity reservation = reservationService.save(_reservation);
		StandardResponse stdResponse = createSuccessResponse("Reservation Saved!");
		stdResponse.setPayload(reservation);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * GET Method Call to Get Reservation for the Customer
	 * 
	 * @return
	 */
    @Operation(summary = "Get The Reservation for the Customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Reservation Request Retrieved!",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
            description = "Invalid Reservation ID",
            content = @Content)
    })
	@GetMapping("/customer/{customerId}")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchReservation(@PathVariable("customerId") String customerId) throws Exception {
		log.debug("|"+name()+"|Request to Get Reservation For the Customer "+customerId);
		List<ReservationEntity> reservations = reservationService.findByCustomerId(customerId);
		StandardResponse stdResponse = createSuccessResponse("Reservation Retrieved. reservations =  "+reservations.size());
		stdResponse.setPayload(reservations);
		return ResponseEntity.ok(stdResponse);
	}

	/**
     * Get the Reservation by Customer ID and Reservation ID
	 * @param customerId
     * @param reservationId
     * @return
     * @throws Exception
	 */
	@Operation(summary = "Get The Reservation for the Customer by Reservation ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Reservation Request Retrieved!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Reservation ID",
					content = @Content)
	})
	@GetMapping("/customer/{customerId}/reservation/{reservationId}")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchReservation(
			@PathVariable("customerId") String customerId, @PathVariable("reservationId") String reservationId) throws Exception {
		log.debug("|"+name()+"|Request to Get Reservation For the Customer "+customerId);
		Optional<ReservationEntity> reservation = reservationService.findById(customerId, reservationId);
		StandardResponse stdResponse = createSuccessResponse("Reservation Retrieved!");
		stdResponse.setPayload(reservation.get());
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Event Handling for Testing Purpose ONLY
	 * @param event
	 * @param customerId
	 * @param reservationId
	 * @return
	 */
	@Operation(summary = "State Machine Demo 2 - Event")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Reservation updated with Event!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to update Reservation for the Event!",
					content = @Content)
	})
	@PostMapping("/event/{event}/customer/{customerId}/reservation/{reservationId}")
	public ResponseEntity<StandardResponse> eventHandlingRequest(@PathVariable("event") String event,
			@PathVariable("customerId") String customerId, @PathVariable("reservationId") String reservationId) {
		log.debug("|"+name()+"|Event Handling for Reservation ID ... "+reservationId);
		ReservationEntity reservation = reservationService.handleEvent(customerId, reservationId, event);
		StandardResponse stdResponse = createSuccessResponse("Reservation Event = "+event);
		stdResponse.setPayload(reservation);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Reservation: RESET Reservation State
	 * ONLY FOR TESTING THE STATE MACHINE
	 *
	 * @param customerId
	 * @param reservationId
	 * @return
	 */
	@Operation(summary = "State Machine Demo 0 - RESET Reservation STATE")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Reservation State Reset!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Reset Reservation State",
					content = @Content)
	})
	@PostMapping("/reset/customer/{customerId}/reservation/{reservationId}")
	public ResponseEntity<StandardResponse> resetReservation(
			@PathVariable("customerId") String customerId, @PathVariable("reservationId") String reservationId) {
		log.debug("|"+name()+"|Request to Reset Reservation State. Reservation ID ... "+reservationId);
		ReservationEntity reservation = reservationService.resetReservation(customerId, reservationId);
		StandardResponse stdResponse = createSuccessResponse("Reservation State Reset!");
		stdResponse.setPayload(reservation);
		return ResponseEntity.ok(stdResponse);
	}

 }