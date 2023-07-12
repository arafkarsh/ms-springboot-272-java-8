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
package io.fusion.air.microservice.domain.entities.order;

import io.fusion.air.microservice.domain.entities.core.springdata.AbstractBaseEntityWithUUID;
import io.fusion.air.microservice.domain.models.order.Cart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Cart Entity with Abstract UUID with Spring Data
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "carts_tx")
public class CartEntity extends AbstractBaseEntityWithUUID {

    @NotNull
    @Column(name = "customerId")
    private String customerId;

    @NotNull
    @Column(name = "productId")
    private String productId;

    @Column(name = "productName")
    private String productName;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "quantity")
    private BigDecimal quantity;

    public CartEntity() {}

    /**
     * Create Cart Entity from Cart Request
     * @param cart
     */
    public CartEntity(Cart cart) {
        this.customerId = cart.getCustomerId();
        this.productId = cart.getProductId();
        this.productName = cart.getProductName();
        this.price = cart.getPrice();
        this.quantity = cart.getQuantity();
    }

    /**
     * Get Customer ID
     * @return
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Get Product ID
     * @return
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Get Product Name
     * @return
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Get Price
     * @return
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Get Quantity
     * @return
     */
    public BigDecimal getQuantity() {
        return quantity;
    }
}
