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
package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.entities.example.OrderItemEntity;
import io.fusion.air.microservice.domain.entities.example.PaymentEntity;
import io.fusion.air.microservice.domain.entities.example.ShippingAddress;
import io.fusion.air.microservice.domain.ports.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

/**
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    private OrderEntity order;

    @BeforeEach
    void setUp() {
        List<OrderItemEntity> items = new ArrayList<OrderItemEntity>();
        items.add(new OrderItemEntity());
        order = OrderEntity.builder()
                .addCustomerId("123")
                .addOrderItems(items)
                .addShippingAddress(new ShippingAddress())
                .addPayment(new PaymentEntity())
                .build();
    }

    @Test
    void requestCreditApproval() {
    }
}
 */