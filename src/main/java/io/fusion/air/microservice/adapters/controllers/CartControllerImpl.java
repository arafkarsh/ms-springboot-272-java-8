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

import io.fusion.air.microservice.domain.entities.order.CartEntity;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.domain.models.order.Cart;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Cart Controller for the Service
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
	 * GET Method Call to ALL CARTS
	 *
	 * @return
	 */
	@Operation(summary = "Get The Carts")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Cart Retrieved!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Cart ID",
					content = @Content)
	})
	@GetMapping("/all")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchCarts() throws Exception {
		log.debug("|"+name()+"|Request to Get Cart For the Customers ");
		List<CartEntity> cart = cartService.findAll();
		StandardResponse stdResponse = createSuccessResponse("Cart Retrieved. Items =  "+cart.size());
		stdResponse.setPayload(cart);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * GET Method Call to Get Cart for the Customer
	 * 
	 * @return
	 */
    @Operation(summary = "Get The Cart for the Customer")
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
		List<CartEntity> cart = cartService.findByCustomerId(customerId);
		StandardResponse stdResponse = createSuccessResponse("Cart Retrieved. Items =  "+cart.size());
		stdResponse.setPayload(cart);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * GET Method Call to Get Cart for the Customer for the Price Greater Than
	 *
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	@Operation(summary = "Get The Cart For Items Price Greater Than")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Cart Retrieved!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Cart ID",
					content = @Content)
	})
	@GetMapping("/customer/{customerId}/price/{price}")
	@ResponseBody
	public ResponseEntity<StandardResponse> fetchCartForItems(@PathVariable("customerId") String customerId,
															  @PathVariable("price") BigDecimal price) throws Exception {
		log.debug("|"+name()+"|Request to Get Cart For the Customer "+customerId);
		List<CartEntity> cart = cartService.fetchProductsByPriceGreaterThan(customerId, price);
		StandardResponse stdResponse = createSuccessResponse("Cart Retrieved. Items =  "+cart.size());
		stdResponse.setPayload(cart);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Add Cart Item to Cart
	 */
	@Operation(summary = "Add Item to Cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Add the Cart Item",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Add the Cart Item",
					content = @Content)
	})
	@PostMapping("/add")
	public ResponseEntity<StandardResponse> addToCart(@Valid @RequestBody Cart _cart) {
		log.debug("|"+name()+"|Request to Add Cart Item... "+_cart.getProductName());
		CartEntity cartItem = cartService.save(_cart);
		StandardResponse stdResponse = createSuccessResponse("Cart Item Added!");
		stdResponse.setPayload(cartItem);
		return ResponseEntity.ok(stdResponse);
	}

 }