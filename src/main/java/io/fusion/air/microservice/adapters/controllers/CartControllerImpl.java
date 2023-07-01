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

import io.fusion.air.microservice.domain.entities.example.CartEntity;
import io.fusion.air.microservice.domain.entities.example.CountryEntity;
import io.fusion.air.microservice.domain.entities.example.CountryGeoEntity;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.domain.ports.services.CartService;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Country Controller for the Service
 *
 * @author arafkarsh
 * @version 1.0
 * 
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/cart")
@RequestScope
@Tag(name = "Cart API", description = "To Manage (Add/Update/Delete/Search) Cart.(io.f.a.m.adapters.controllers.CartControllerImpl)")
public class CartControllerImpl extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());
	
	@Autowired
	private ServiceConfiguration serviceConfig;
	private String serviceName;

	@Autowired
	private CartService cartService;

	/**
	 * GET Method Call to Get All the Geo Countries with Page and Size
	 * 
	 * @return
	 */
    @Operation(summary = "Get The Cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Cart Retrieved!",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
            description = "Invalid Cart ID",
            content = @Content)
    })
	@GetMapping("/customer/{customerId}")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchCart(@PathVariable("customerId") String customerId) throws Exception {
		log.debug("|"+name()+"|Request to Get Cart For the Customer "+customerId);

		Optional<CartEntity> cart = cartService.findByCustomerId(customerId);
		if(cart.isPresent()) {
			StandardResponse stdResponse = createSuccessResponse("Data Fetch Success!");
			stdResponse.setPayload(cart.get());
			return ResponseEntity.ok(stdResponse);
		}
		throw new DataNotFoundException(("Cart is Empty!"));
	}

 }