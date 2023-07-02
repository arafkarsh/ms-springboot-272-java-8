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
package io.fusion.air.microservice.domain.entities.example;

import io.fusion.air.microservice.domain.entities.core.springdata.AbstractBaseEntityWithUUID;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Entity
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Entity
@Table(name = "order_tx")
public class OrderEntity extends AbstractBaseEntityWithUUID {

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "totalOrderValue")
    private BigDecimal totalOrderValue;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    @Embedded
    private ShippingAddress shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "uuid")
    private PaymentEntity payment;

    private OrderEntity() {
    }

    public BigDecimal calculateTotalOrderValue() {
        /**
        if(orderItems != null && orderItems.size() > 0) {
            for(OrderItemEntity i : orderItems) {
                totalOrderValue = totalOrderValue.add(i.getPrice());
            }
        }
         */
        if(orderItems != null) {
            totalOrderValue = orderItems.stream()
                    .map(OrderItemEntity::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        System.out.println("Order Customer ID = "+customerId+" Total Order Value = "+totalOrderValue);
        return totalOrderValue;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final OrderEntity order;

        private Builder() {
            order = new OrderEntity();
        }

        public Builder addCustomerId(String customerId) {
            order.customerId = customerId;
            return this;
        }

        public Builder addOrderItems(List<OrderItemEntity> orderItems) {
            order.orderItems = orderItems;
            return this;
        }

        public Builder addShippingAddress(ShippingAddress shippingAddress) {
            order.shippingAddress = shippingAddress;
            return this;
        }

        public Builder addPayment(PaymentEntity payment) {
            order.payment = payment;
            return this;
        }

        public OrderEntity build() {
            return order;
        }
    }
}
