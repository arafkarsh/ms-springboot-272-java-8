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

import io.fusion.air.microservice.adapters.repository.OrderRepository;
import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.ports.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Order Service
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * ONLY FOR TESTING PURPOSE
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderEntity> findAll() {
        return (List<OrderEntity>) orderRepository.findAll();
    }

    /**
     * Find Order by Customer ID
     *
     * @param customerId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderEntity> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    /**
     * Find by Order by Customer ID
     *
     * @param itemId
     * @param customerId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderEntity> findById(String itemId, String customerId) {
        return Optional.empty();
    }

    /**
     * Find By Order by Customer ID
     *
     * @param itemId
     * @param customerId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderEntity> findById(UUID itemId, String customerId) {
        return Optional.empty();
    }

    /**
     * Save the Cart Item
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public OrderEntity save(OrderEntity order) {
        if(order == null) {
            throw new InputDataException("Invalid Order Data");
        }
        order.calculateTotalOrderValue();
        return orderRepository.save(order);
    }
}
