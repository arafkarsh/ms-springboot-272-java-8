/**
 * (C) Copyright 2021 Araf Karsh Hamid
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

import io.fusion.air.microservice.adapters.security.AuthorizationRequired;
import io.fusion.air.microservice.domain.entities.order.ProductEntity;
import io.fusion.air.microservice.domain.exceptions.*;
import io.fusion.air.microservice.domain.models.core.StandardResponse;
import io.fusion.air.microservice.domain.models.order.PaymentDetails;
import io.fusion.air.microservice.domain.models.order.PaymentStatus;
import io.fusion.air.microservice.domain.models.order.PaymentType;
import io.fusion.air.microservice.domain.models.order.Product;
import io.fusion.air.microservice.domain.ports.services.ProductService;
import io.fusion.air.microservice.server.config.ServiceConfiguration;
import io.fusion.air.microservice.server.controllers.AbstractController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Product Controller for the Service
 *
 * Only Selected Methods will be secured in this packaged - which are Annotated with
 * @AuthorizationRequired
 * @Operation(summary = "Cancel Product", security = { @SecurityRequirement(name = "bearer-key") })
 * 
 * @author arafkarsh
 * @version 1.0
 * 
 */
@CrossOrigin
@Configuration
@RestController
// "/ms-cache/api/v1"
@RequestMapping("${service.api.path}/product")
@RequestScope
@Tag(name = "Product API", description = "To Manage (Add/Update/Delete/Search) Products.(io.f.a.m.adapters.controllers.ProductControllerImpl)")
public class ProductControllerImpl extends AbstractController {

	// Set Logger -> Lookup will automatically determine the class name.
	private static final Logger log = getLogger(lookup().lookupClass());
	
	@Autowired
	private ServiceConfiguration serviceConfig;
	private String serviceName;

	@Autowired
	ProductService productServiceImpl;

	/**
	 * Create the Product
	 */
	@Operation(summary = "Create Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Create the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "404",
					description = "Unable to Create the Product",
					content = @Content)
	})
	@PostMapping("/create")
	public ResponseEntity<StandardResponse> createProduct(@Valid @RequestBody Product _product) {
		log.debug("|"+name()+"|Request to Create Product... "+_product);
		log.info(getClaims().getSubject());

		ProductEntity prodEntity = productServiceImpl.createProduct(_product);
		StandardResponse stdResponse = createSuccessResponse("Product Created");
		stdResponse.setPayload(prodEntity);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * GET Method Call to Check the Product Status
	 * 
	 * @return
	 */
    @Operation(summary = "Get the Product By Product UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Product Retrieved for status check",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
            description = "Invalid Product ID.",
            content = @Content)
    })
	@GetMapping("/status/{productId}")
	@ResponseBody
	public ResponseEntity<StandardResponse> getProductStatus(@PathVariable("productId") UUID _productId,
														HttpServletRequest request,
														HttpServletResponse response) throws Exception {
		log.debug("|"+name()+"|Request to Get Product Status.. "+_productId);

		ProductEntity product = productServiceImpl.getProductById(_productId);
		StandardResponse stdResponse = createSuccessResponse("Data Fetch Success!");
		stdResponse.setPayload(product);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * GET Method Call to Get All the Products
	 *
	 * @return
	 */
	@Operation(summary = "Get All the Products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "List All the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Product Reference No.",
					content = @Content)
	})
	@GetMapping("/all/")
	@ResponseBody
	public ResponseEntity<StandardResponse> getAllProducts(HttpServletRequest request,
														   HttpServletResponse response) throws Exception {
		log.debug("|"+name()+"|Request to get All Products ... ");
		List<ProductEntity> productList = productServiceImpl.getAllProduct();
		StandardResponse stdResponse = null;
		log.info("Products List = "+productList.size());
		if(productList == null || productList.isEmpty()) {
			productList = createFallBackProducts();
			stdResponse = createSuccessResponse("201","Fallback Data! Records = "+productList.size());
		} else {
			stdResponse = createSuccessResponse("Data Fetch Success! Records = "+productList.size());
		}
		stdResponse.setPayload(productList);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * GET Method Call to Get All the Products
	 *
	 * @return
	 */
	@Operation(summary = "Get All the Products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "List All the Product",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Invalid Product Reference No.",
					content = @Content)
	})
	@GetMapping("/all/leak")
	@ResponseBody
	public ResponseEntity<StandardResponse> getAllProductsWithLeak(HttpServletRequest request,
														   HttpServletResponse response) throws Exception {
		log.debug("|"+name()+"|Request to get All Products ... ");
		List<ProductEntity> productList = productServiceImpl.getAllProduct();
		StandardResponse stdResponse = null;
		log.info("Products List = "+productList.size());
		if(productList == null || productList.isEmpty()) {
			productList = createFallBackProducts();
			stdResponse = createSuccessResponse("201","Fallback Data! Records = "+productList.size());
		} else {
			stdResponse = createSuccessResponse("Data Fetch Success! Records = "+productList.size());
		}
		stdResponse.setPayload(productList);

		// For Memory Leak Testing
		productServiceImpl.leakMemory( productList);

		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Search the Product by Product Name
	 */
	@Operation(summary = "Search Product By Product Name")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product(s) Found!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Find the Product(s)!",
					content = @Content)
	})
	@GetMapping("/search/product/{productName}")
	public ResponseEntity<StandardResponse> searchProductsByName(@PathVariable("productName") String _productName) {
		log.debug("|"+name()+"|Request to Search the Product By Name ... "+_productName);
		List<ProductEntity> products = productServiceImpl.fetchProductsByName(_productName);
		StandardResponse stdResponse = createSuccessResponse("Products Found  ("+products.size()+") For Search Term = "+_productName);
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Specification Pattern Example
	 * Search the Product by Product Name and Product Price
	 */
	@Operation(summary = "Search Product By Product Name")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product(s) Found!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Find the Product(s)!",
					content = @Content)
	})
	@GetMapping("/search/product/{productName}/price/{productPrice}")
	public ResponseEntity<StandardResponse> searchProductsByNameAndPrice(
			@PathVariable("productName") String _productName,
			@PathVariable("productPrice") BigDecimal _productPrice) {
		log.debug("|"+name()+"|Request to Search the Product By Name ... "+_productName);
		List<ProductEntity> products = productServiceImpl.findProducts(_productName, _productPrice);
		StandardResponse stdResponse = createSuccessResponse("Products Found ("+products.size()+") For Search Keys: Phone = "
				+_productName + " Price = "+_productPrice);
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Specification Pattern Example
	 * Search the Product by Product Name, Product Price & Location
	 */
	@Operation(summary = "Search Product By Product Name, Location and Price Equal To")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product(s) Found!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Find the Product(s)!",
					content = @Content)
	})
	@GetMapping("/search/product/{productName}/price/{productPrice}/location/{location}")
	public ResponseEntity<StandardResponse> searchProductsByNameAndPriceAndLocation(
			@PathVariable("productName") String _productName,
			@PathVariable("productPrice") BigDecimal _productPrice,
			@PathVariable("location") String _location) {
		log.debug("|"+name()+"|Request to Search the Product By Name ... "+_productName);
		List<ProductEntity> products = productServiceImpl.findProducts(_productName, _productPrice, _location);
		StandardResponse stdResponse = createSuccessResponse("Products Found ("+products.size()+") For Search Keys: Phone = "
				+_productName + " Price = "+_productPrice + " Location = "+_location);
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Specification Pattern Example
	 * Search the Product by Product Name & Location and Price Greater Than
	 */
	@Operation(summary = "Search Product By Product Name & Location and Price Greater Than")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product(s) Found!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Find the Product(s)!",
					content = @Content)
	})
	@GetMapping("/search/product/{productName}/location/{location}/price/{productPrice}")
	public ResponseEntity<StandardResponse> findProductsAndPriceGreaterThan(
			@PathVariable("productName") String _productName,
			@PathVariable("location") String _location,
			@PathVariable("productPrice") BigDecimal _productPrice) {
		log.debug("|"+name()+"|Request to Search the Product By Name ... "+_productName);
		List<ProductEntity> products = productServiceImpl.findProductsAndPriceGreaterThan(
																_productName, _location, _productPrice);
		StandardResponse stdResponse = createSuccessResponse("Products Found ("+products.size()
				+") For Search Keys: Phone = " +_productName + " Price = "+_productPrice + " Location = "+_location);
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Search the Product by Product price
	 */
	@Operation(summary = "Search Product By Product Price Greater Than or Equal To")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product(s) Found!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Find the Product(s)!",
					content = @Content)
	})
	@GetMapping("/search/price/{price}")
	public ResponseEntity<StandardResponse> searchProductsByPrice(@PathVariable("price") BigDecimal _price) {
		log.debug("|"+name()+"|Request to Search the Product By Price... "+_price);
		List<ProductEntity> products = productServiceImpl.fetchProductsByPriceGreaterThan(_price);
		StandardResponse stdResponse = createSuccessResponse("Products Found for Price >= "+_price);
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Search Active Products
	 */
	@Operation(summary = "Search Active Products")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product(s) Found!",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Find the Product(s)!",
					content = @Content)
	})
	@GetMapping("/search/active/")
	public ResponseEntity<StandardResponse> searchActiveProducts() {
		log.debug("|"+name()+"|Request to Search the Active Products ... ");
		List<ProductEntity> products = productServiceImpl.fetchActiveProducts();
		StandardResponse stdResponse = createSuccessResponse("Active Products Found = "+products.size());
		stdResponse.setPayload(products);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * De-Activate the Product
	 */
	@Operation(summary = "De-Activate Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product De-Activated",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to De-Activate the Product",
					content = @Content)
	})
	@PutMapping("/deactivate/{productId}")
	public ResponseEntity<StandardResponse> deActivateProduct(@PathVariable("productId") UUID _productId) {
		log.debug("|"+name()+"|Request to De-Activate the Product... "+_productId);
		ProductEntity product = productServiceImpl.deActivateProduct(_productId);
		StandardResponse stdResponse = createSuccessResponse("Product De-Activated");
		stdResponse.setPayload(product);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Activate the Product
	 */
	@Operation(summary = "Activate Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product Activated",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Activate the Product",
					content = @Content)
	})
	@PutMapping("/activate/{productId}")
	public ResponseEntity<StandardResponse> activateProduct(@PathVariable("productId") UUID _productId) {
		log.debug("|"+name()+"|Request to Activate the Product... "+_productId);
		ProductEntity product = productServiceImpl.activateProduct(_productId);
		StandardResponse stdResponse = createSuccessResponse("Product Activated");
		stdResponse.setPayload(product);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Update the Product Details
	 * This API Can be tested for Optimistic Lock Exceptions as the Entity is a Versioned Entity
	 */
	@Operation(summary = "Update the Product")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product Updated",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Update the Product",
					content = @Content)
	})
	@PutMapping("/update/")
	public ResponseEntity<StandardResponse> updateProduct(@Valid @RequestBody ProductEntity _product) {
		log.debug("|"+name()+"|Request to Update Product Details... "+_product);
		ProductEntity prodEntity = productServiceImpl.updateProduct(_product);
		StandardResponse stdResponse = createSuccessResponse("Product Updated!");
		stdResponse.setPayload(prodEntity);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Update the Product Price
	 */
	@Operation(summary = "Update the Product Price")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product Price Updated",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Update the Product Price",
					content = @Content)
	})
	@PutMapping("/update/price")
	public ResponseEntity<StandardResponse> updatePrice(@Valid @RequestBody ProductEntity _product) {
		log.debug("|"+name()+"|Request to Update Product Price... ["+_product);
		ProductEntity prodEntity = productServiceImpl.updatePrice(_product);
		StandardResponse stdResponse = createSuccessResponse("Product Price Updated");
		stdResponse.setPayload(prodEntity);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Update the Product Details
	 */
	@Operation(summary = "Update the Product Details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product Details Updated",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Update the Product Details",
					content = @Content)
	})
	@PutMapping("/update/details")
	public ResponseEntity<StandardResponse> updateProductDetails(@Valid @RequestBody ProductEntity _product) {
		log.debug("|"+name()+"|Request to Update Product Details... "+_product);
		ProductEntity prodEntity = productServiceImpl.updateProductDetails(_product);
		StandardResponse stdResponse = createSuccessResponse("Product Details Updated");
		stdResponse.setPayload(prodEntity);
		return ResponseEntity.ok(stdResponse);
	}

	/**
	 * Delete the Product
	 */
	@AuthorizationRequired(role = "User")
	@Operation(summary = "Delete Product", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product Deleted",
					content = {@Content(mediaType = "application/json")}),
			@ApiResponse(responseCode = "400",
					description = "Unable to Delete the Product",
					content = @Content)
	})
	@DeleteMapping("/{productId}")
	public ResponseEntity<StandardResponse> deleteProduct(@PathVariable("productId") UUID _productId) {
		log.debug("|"+name()+"|Request to Delete Product... "+_productId);
		productServiceImpl.deleteProduct(_productId);
		StandardResponse stdResponse = createSuccessResponse("Product Deleted!");
		return ResponseEntity.ok(stdResponse);
	}


	/**
	 * Create Fall Back Product for Testing Purpose ONLY
	 * @return
	 */
	private List<ProductEntity> createFallBackProducts() {
		List<ProductEntity> productList = new ArrayList<ProductEntity>();
		productList.add(new ProductEntity("iPhone 10", "iPhone 10, 64 GB RED", new BigDecimal(60000), "12345"));
		productList.add(new ProductEntity("iPhone 10", "iPhone 10, 64 GB BLACK", new BigDecimal(60000), "12345"));
		productList.add(new ProductEntity("iPhone 10", "iPhone 10, 64 GB GOLD", new BigDecimal(60000), "12345"));
		productList.add(new ProductEntity("iPhone 10", "iPhone 10, 128 GB GOLD", new BigDecimal(65000), "12345"));
		productList.add(new ProductEntity("iPhone 10", "iPhone 10, 1256 GB GOLD", new BigDecimal(75000), "12345"));
		productList.add(new ProductEntity("iPhone 11", "iPhone 11, 128 GB", new BigDecimal(70000), "12345"));
		productList.add(new ProductEntity("iPhone 11", "iPhone 11, 256 GB", new BigDecimal(80000), "12345"));
		productList.add(new ProductEntity("iPhone 12", "iPhone 12, 128 GB", new BigDecimal(80000), "12345"));
		productList.add(new ProductEntity("iPhone 12", "iPhone 12, 256 GB", new BigDecimal(90000), "12345"));
		productList.add(new ProductEntity("Samsung Galaxy s20", "Samsung Galaxy s20, 256 GB", new BigDecimal(80000), "12345"));
		productList.add(new ProductEntity("Samsung Galaxy s21", "Samsung Galaxy s21, 256 GB", new BigDecimal(90000), "12345"));


		try {
			productServiceImpl.createProductsEntity(productList);
			productList = productServiceImpl.getAllProduct();
		} catch (Exception ignored) { ignored.printStackTrace();}
		return productList;
	}

	/**
	 * Process the Product
	 * To Demonstrate Exception Handling.
	 * The Error Code for the Exceptions will be automatically determined by the Framework.
	 * Error Code will be Different for Each Microservice.
	 */
    /**
	@Operation(summary = "Process Product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Process the payment",
            content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
            description = "Unable to process the Product",
            content = @Content)
    })
	 */
	@Operation(
			summary = "Process Product",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Process the payment",
							content = @Content(
									mediaType = "application/json",
									schema = @Schema(implementation = ResponseEntity.class))
					),
					@ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
					@ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
					@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
			},
			parameters = {
					@Parameter(
							name = "x-csrf-header",
							in = ParameterIn.HEADER,
							description = "CSRF-HEADER",
							required = false,
							schema = @Schema(type = "string", defaultValue = "X-XSRF-TOKEN")
					),
					@Parameter(
							name = "x-csrf-param",
							in = ParameterIn.HEADER,
							description = "CSRF-PARAM",
							required = false,
							schema = @Schema(type = "string", defaultValue = "_csrf")
					),
					@Parameter(
							name = "x-csrf-token",
							in = ParameterIn.HEADER,
							description = "CSRF-TOKEN",
							required = false,
							schema = @Schema(type = "string", defaultValue = "2072dc75-d126-4442-a006-1f657c8973c2")
					)
			}
	)
	@PostMapping("/processProducts")
    public ResponseEntity<StandardResponse> processProduct(@RequestBody PaymentDetails _payDetails) {
		log.debug("|"+name()+"|Request to process Product... "+_payDetails);
		if(_payDetails != null && _payDetails.getOrderValue() > 0) {
			StandardResponse stdResponse = createSuccessResponse("Processing Success!");
			PaymentStatus ps = new PaymentStatus(
					"fb908151-d249-4d30-a6a1-4705729394f4",
					LocalDateTime.now(),
					"Accepted",
					UUID.randomUUID().toString(),
					LocalDateTime.now(),
					PaymentType.CREDIT_CARD);
			stdResponse.setPayload(ps);
			return ResponseEntity.ok(stdResponse);
		} else {
			 // throw new DuplicateDataException("Invalid Order Value");
			throw new InputDataException("Invalid Order Value");
			// throw new BusinessServiceException("Invalid Order Value");
			// throw new ControllerException("Invalid Order Value");
			// throw new ResourceNotFoundException("Invalid Order Value");
			// throw new RuntimeException("Invalid Order Value");

		}
    }

 }