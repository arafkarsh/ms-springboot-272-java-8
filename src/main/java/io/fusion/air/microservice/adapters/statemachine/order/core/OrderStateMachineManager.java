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
package io.fusion.air.microservice.adapters.statemachine.order.core;
// Custom
import io.fusion.air.microservice.adapters.statemachine.order.config.OrderStateChangeInterceptor;
import io.fusion.air.microservice.adapters.statemachine.order.config.OrderStateDetails;
import io.fusion.air.microservice.domain.entities.order.OrderEntity;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.ports.services.OrderStateMachineService;
import io.fusion.air.microservice.domain.statemachine.order.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
// Java
import org.slf4j.Logger;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

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
@RequestScope
public class OrderStateMachineManager implements OrderStateMachineService {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;

    @Autowired
    private OrderStateChangeInterceptor orderStateChangeInterceptor;

    @Autowired
    private OrderStateDetails orderStateDetails;

    /**
     * STEP 1:
     * Credit Check Event
     * @param order
     * @return
     */
    @Override
    public void creditCheckRequest(OrderEntity order) {
        sendEvent(OrderEvent.CREDIT_CHECKING_EVENT, order);
    }

    /**
     * Credit Approved Event
     * @param order
     */
    @Override
    public void creditApproved(OrderEntity order) {
        sendEvent(OrderEvent.CREDIT_APPROVED_EVENT, order);
    }

    /**
     * Credit Declined Event
     * @param order
     */
    @Override
    public void creditDeclined(OrderEntity order) {
        sendEvent(OrderEvent.CREDIT_DECLINED_EVENT, order);
    }

    /**
     * STEP 2:
     * Payment Init Event
     * @param order
     * @return
     */
    @Override
    public void paymentInit(OrderEntity order) {
        sendEvent(OrderEvent.PAYMENT_INIT_EVENT, order);
    }

    /**
     * Payment Approved
     * @param order
     * @return
     */
    @Override
    public void paymentApproved(OrderEntity order) {
        sendEvent(OrderEvent.PAYMENT_APPROVED_EVENT, order);
    }

    /**
     * Payment Declined Event
     * @param order
     * @return
     */
    @Override
    public void paymentDeclined(OrderEntity order) {
        sendEvent(OrderEvent.PAYMENT_DECLINED_EVENT, order);
    }

    /**
     * Package Fork - Parallel Processing: Order Packaging
     *
     * @param order
     * @return
     */
    @Override
    public void packageFork(OrderEntity order) {
        sendEvent(OrderEvent.PACKAGE_FORK_EVENT, order);
    }

    /**
     * Package the Order
     * @param order
     * @return
     */
    @Override
    public void orderPackage(OrderEntity order) {
        sendEvent(OrderEvent.PACKAGE_EVENT, order);
    }

    /**
     * Package Fork - Parallel Processing: Send Bill Notification
     *
     * @param order
     * @return
     */
    @Override
    public void sendBill(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_SEND_BILL_EVENT, order);
    }

    /**
     * Ready to Ship once the Order is Packaged
     *
     * @param order
     * @return
     */
    @Override
    public void readyToShip(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_READY_TO_SHIP_EVENT, order);
    }

    /**
     * Ship the Product Event
     *
     * @param order
     * @return
     */
    @Override
    public void shipTheProduct(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_SHIPPED_EVENT, order);
    }

    /**
     * Order In Transit
     *
     * @param order
     * @return
     */
    @Override
    public void orderInTransit(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_IN_TRANSIT_EVENT, order);
    }

    /**
     * Send For Delivery Event
     *
     * @param order
     * @return
     */
    @Override
    public void sendForDelivery(OrderEntity order) {
        sendEvent(OrderEvent.SEND_FOR_DELIVERY_EVENT, order);
    }

    /**
     * Order Delivered Event
     *
     * @param order
     */
    @Override
    public void orderDelivered(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_DELIVERED_EVENT, order);
    }

    /**
     * Order Returned Event
     *
     * @param order
     */
    @Override
    public void orderReturned(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_RETURNED_EVENT, order);
    }

    /**
     * Order Cancelled Event
     * @param order
     */
    public void orderCancelled(OrderEntity order) {
        sendEvent(OrderEvent.ORDER_CANCELLED_EVENT, order);
    }

    /**
     * Sends Multiple Events to the State Machine
     * @param order
     * @param events
     */
    public void multipleEvents(OrderEntity order, List<OrderEvent> events) {
        sendEvent(order, events);
    }

    // =======================================================================================================
    // Restore State Machine and Send Message / Event to State Machine
    // =======================================================================================================

    /**
     * Send Message/Event to State Machine
     * @param event
     * @param order
     */
    private void sendEvent(OrderEvent event, OrderEntity order) {
       validateInputs(event, order);
       orderStateDetails.addOrder(order);
       orderStateDetails.setEvent(event);
        // Restore the state Machine based on Order ID
        StateMachine<OrderState, OrderEvent> sm = restoreStateMachine(order);
        // Create Message with the Order Event and Set the Order ID in the Header
        Message mesg = MessageBuilder.withPayload(event)
                .setHeader(OrderConstants.ORDER_ID_HEADER, order.getOrderId())
                // This is NOT Required as Order is set as an Extended State
                // Check Restore State Machine Method
                // .setHeader(OrderConstants.ORDER_HEADER, order)
                .build();
        // Send the Message to the State Machine
        // System.out.println("[0][1][1] - OrderStateMachineManager: "+event.name());
        sm.sendEvent(mesg);
    }

    /**
     * Send Multiple Events
     * @param order
     * @param events
     */
    private void sendEvent(OrderEntity order, List<OrderEvent> events ) {
        validateInputs(events, order);
        // Restore the state Machine based on Order ID
        StateMachine<OrderState, OrderEvent> sm = restoreStateMachine(order);
        orderStateDetails.addOrder(order);
        // Send Multiple Events
        for(OrderEvent event : events) {
            orderStateDetails.setEvent(event);
            // Create Message with the Order Event and Set the Order ID in the Header
            Message mesg = MessageBuilder.withPayload(event)
                    .setHeader(OrderConstants.ORDER_ID_HEADER, order.getOrderId())
                    .build();
            // Send the Message to the State Machine
            // System.out.println("[0][1][1] - OrderStateMachineManager: " + event.name());
            sm.sendEvent(mesg);
        }
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
        log.info("Restore State Machine For Order Id = "+order.getOrderId()+" OrderState = "+order.getOrderState());
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
        // Set the Order in the Extended State of the State Machine
        sm.getExtendedState().getVariables().put(OrderConstants.ORDER_HEADER, order);
        // Start the State Machine with new Order State (Set from DB)
        sm.start();
        // Return the State  Machine
        return sm;
    }

    /**
     * Validate Inputs
     * @param event
     * @param order
     * @return
     */
    private boolean validateInputs(OrderEvent event, OrderEntity order) {
        if(event == null) {
            throw new InputDataException("Invalid OrderEvent! to Send Event");
        }
        if(order == null) {
            throw new InputDataException("Invalid Order! to Send Event");
        }
        return true;
    }

    private boolean validateInputs(List<OrderEvent> events, OrderEntity order) {
        if(events == null) {
            throw new InputDataException("Invalid OrderEvent! to Send Event");
        }
        if(order == null) {
            throw new InputDataException("Invalid Order! to Send Event");
        }
        return true;
    }
}
