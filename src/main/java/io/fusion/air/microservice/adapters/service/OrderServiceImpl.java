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
// Custom
import io.fusion.air.microservice.adapters.repository.OrderRepository;
import io.fusion.air.microservice.adapters.statemachine.OrderStateChangeInterceptor;
import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.ports.services.OrderService;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
// Spring State Machine
import io.fusion.air.microservice.utils.Utils;
import org.slf4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// Java
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order Service
 * Order Processing is implemented with Spring State Machine
 * 1. Request for Credit
 * 2. Payment Processing
 * 3. Shipping the Product
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class OrderServiceImpl implements OrderService {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    // Message Header for the Events used in the State Machine
    public static final String ORDER_ID_HEADER = "ORDER_ID";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;

    @Autowired
    private OrderStateChangeInterceptor orderStateChangeInterceptor;


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
     * Find by Order by Customer ID and Order ID
     *
     * @param customerId
     * @param orderId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderEntity> findById(String customerId, String orderId) {
        Optional<OrderEntity> o = orderRepository.findByCustomerIdAndOrderId(customerId, Utils.getUUID(orderId));
        if(o.isPresent()) {
            return o;
        }
        throw new DataNotFoundException("Order Not Found for OrderId="+orderId);
    }

    /**
     * Find By Order by Customer ID and Order ID
     *
     * @param customerId
     * @param orderId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderEntity> findById(String customerId, UUID orderId) {
        Optional<OrderEntity> o = orderRepository.findByCustomerIdAndOrderId(customerId, orderId);
        if(o.isPresent()) {
            return o;
        }
        throw new DataNotFoundException("Order Not Found for OrderId="+orderId);    }

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

    /**
     * Process Credit Approval for the Order
     *
     * @param customerId
     * @param orderId
     * @return
     */
    public OrderEntity processCreditApproval(String customerId, String orderId) {
        Optional<OrderEntity> orderOpt = findById( customerId,  orderId);
        log.info("[1] Process Order ID = "+orderId);
        requestCreditApproval(orderOpt.get());
        return orderOpt.get();
    }

    /**
     * Request for Credit Approval
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public StateMachine<OrderState, OrderEvent> requestCreditApproval(OrderEntity order) {
        log.info("[2] Restoring State Machine for = "+order.getOrderId());
        StateMachine<OrderState, OrderEvent> sm = restoreStateMachine(order);
        log.info("[3] Sending Message For Order Id = "+order.getOrderId());
        sendEvent(sm, OrderEvent.CREDIT_CHECKING_EVENT, order);
        return sm;
    }

    /**
     * Process the Payment
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public StateMachine<OrderState, OrderEvent> processPayment(OrderEntity order) {
        StateMachine<OrderState, OrderEvent> sm = restoreStateMachine(order);
        sendEvent(sm, OrderEvent.CONFIRM_PAYMENT_EVENT, order);
        return sm;
    }

    /**
     * Ship the Product
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public StateMachine<OrderState, OrderEvent> shipTheProduct(OrderEntity order) {
        StateMachine<OrderState, OrderEvent> sm = restoreStateMachine(order);
        sendEvent(sm, OrderEvent.ORDER_SHIPPED_EVENT, order);
        return sm;
    }

    private void sendEvent(StateMachine<OrderState, OrderEvent> sm, OrderEvent event, OrderEntity order) {
        if(sm == null) {
            throw new InputDataException("Invalid State Machine! to Send Event");
        }
        if(event == null) {
            throw new InputDataException("Invalid OrderEvent! to Send Event");
        }
        if(order == null) {
            throw new InputDataException("Invalid Order! to Send Event");
        }
        // Create Message with the Order Event and Set the Order ID in the Header
        Message mesg = MessageBuilder.withPayload(event)
                .setHeader(ORDER_ID_HEADER, order.getOrderId())
                .build();
        // Send the Message to the State Machine
        sm.sendEvent(mesg);
    }

    /**
     * Return the State Machine with Current Order State
     *
     * @param customerId
     * @param orderId
     * @return
     */
    private StateMachine<OrderState, OrderEvent> restoreStateMachine(String customerId, String orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findByCustomerIdAndOrderId(customerId, Utils.getUUID(orderId));
        if(orderOpt.isPresent()) {
            return restoreStateMachine(orderOpt.get());
        }
        throw new DataNotFoundException("Order ID ["+orderId+"] NOT FOUND For Customer = "+customerId);
    }

    /**
     * Return the State Machine with Current Order State
     *
     * @param order
     * @return
     */
    private StateMachine<OrderState, OrderEvent> restoreStateMachine(OrderEntity order) {
        if(order == null) {
            throw new InputDataException("Invalid Order (null) to Restore the State Machine!");
        }
        log.info("[2.1] Get State Machine For Order Id = "+order.getOrderId());
        StateMachine<OrderState, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getOrderId());
        // Stop The State Machine
        sm.stop();
        // Set the State (Order) retrieved from the Database
        sm.getStateMachineAccessor()
            .doWithAllRegions(sma -> {
                // Add State Change Interceptor
                sma.addStateMachineInterceptor(orderStateChangeInterceptor);
                // Set the Current Order State with State Machine
                sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderState(),
                            null, null, null));
                }
            );
        // Start the State Machine with new Order State (Set from DB)
        sm.start();
        // Return the State  Machine
        return sm;
    }
}
