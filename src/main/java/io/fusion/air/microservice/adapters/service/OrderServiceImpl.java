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
import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.ports.services.OrderService;
import io.fusion.air.microservice.domain.ports.services.OrderStateMachineService;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
import io.fusion.air.microservice.utils.Utils;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// Java
import org.slf4j.Logger;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStateMachineService orderStateMachineService;

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
     * Reset the Order State to Initialized
     * THIS METHOD IS ONLY FOR TESTING THE STATE MACHINE BY RESETTING THE ORDER BACK TO ITS INIT STATE.
     * @param customerId
     * @param orderId
     * @return
     */
    @Transactional
    public OrderEntity resetOrder(String customerId, String orderId) {
        Optional<OrderEntity> orderOpt = findById( customerId,  orderId);
        log.info("Reset Order ID = "+orderId);
        if(orderOpt.isPresent()) {
            OrderEntity order = orderOpt.get();
            order.resetOrderState();
            orderRepository.save(order);
            return order;
        }
        throw new DataNotFoundException("Order Not Found for "+orderId);
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
        orderStateMachineService.requestCreditApproval(orderOpt.get());
        return orderOpt.get();
    }

    /**
     * For Testing Purpose Only
     * Handle Event - Generic Method
     *
     * @param customerId
     * @param orderId
     * @param event
     * @return
     */
    public OrderEntity handleEvent(String customerId, String orderId, String event) {
        OrderEvent orderEvent = OrderEvent.fromString(event);
        return handleEvent(customerId, orderId, orderEvent);
    }

    /**
     * For Testing Purpose Only
     * Handle Event - Generic Method
     *
     * @param customerId
     * @param orderId
     * @param orderEvent
     * @return
     */
    public OrderEntity handleEvent(String customerId, String orderId, OrderEvent orderEvent) {
        Optional<OrderEntity> orderOpt = findById( customerId,  orderId);
        log.info("Handle Event "+orderEvent+" For Order ID = "+orderId);
        OrderEntity order = orderOpt.get();
        switch(orderEvent) {
            case CREDIT_CHECKING_EVENT:
                orderStateMachineService.requestCreditApproval(order);
                break;

            case CREDIT_APPROVED_EVENT:
                orderStateMachineService.creditApproved(order);
                break;

            case CREDIT_DECLINED_EVENT:
                orderStateMachineService.creditDeclined(order);
                break;

            case PAYMENT_INIT_EVENT:
                orderStateMachineService.processPayment(order);
                break;

            case PAYMENT_APPROVED_EVENT:
                orderStateMachineService.paymentApproved(order);
                break;

            case PAYMENT_DECLINED_EVENT:
                orderStateMachineService.paymentDeclined(order);
                break;

            case ORDER_SHIPPED_EVENT:
                orderStateMachineService.shipTheProduct(order);
                break;

            case SEND_FOR_DELIVERY_EVENT:
                orderStateMachineService.sendForDelivery(order);
                break;

            case ORDER_CANCELLED_EVENT:
                orderStateMachineService.orderCancelled(order);
                break;

            case ORDER_DELIVERED_EVENT:
                orderStateMachineService.orderDelivered(order);
                break;

            case ORDER_RETURNED_EVENT:
                orderStateMachineService.orderReturned(order);
                break;

            default:
                break;
        }
        return order;
    }

    /**
     * Process the Payment Request with External System
     * @param customerId
     * @param orderId
     * @return
     */
    public OrderEntity processPaymentRequest(String customerId, String orderId) {
        Optional<OrderEntity> orderOpt = findById( customerId,  orderId);
        log.info("Process Order ID = "+orderId);
        orderStateMachineService.processPayment(orderOpt.get());
        return orderOpt.get();
    }
}
