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
import io.fusion.air.microservice.domain.entities.order.OrderEntity;
import io.fusion.air.microservice.domain.exceptions.BusinessServiceException;
import io.fusion.air.microservice.domain.statemachine.order.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
// Java
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order State Machine Actions
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Component
public class OrderStateMachineActions {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Bean
    public Action<OrderState, OrderEvent> creditCheckAction() {
        return context -> {
            logStateTransition(context, "ACTION ON CREDIT CHECK EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send Credit Approval Request Using Kafka Topic
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> creditApprovedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON CREDIT APPROVED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for Credit Approval
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean
    public Action<OrderState, OrderEvent> creditDeniedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON CREDIT DENIED EVENT - THROWS EXCEPTION");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for Credit Denied
            throw new BusinessServiceException("Service Unavailable to check Credit: Target State = "+context.getTarget().getId().name());
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> paymentInitAction() {
        return context -> {
            logStateTransition(context, "ACTION ON PAYMENT INIT EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send Payment Request to Payment Gateway Service / Usign a Kafka Topic
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> paymentApprovalAction() {
        return context -> {
            logStateTransition(context, "ACTION ON PAYMENT APPROVAL EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Payment Approval
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> paymentDeclinedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON PAYMENT DECLINED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Payment Declined
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderPackagingAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER PACKAGING EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> sendBillAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER SEND BILL EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderReadyToShipAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER READY TO SHIP EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderShippedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER SHIPPED EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderInTransitAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER IN TRANSIT EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> sendForDelivery() {
        return context -> {
            logStateTransition(context, "ACTION ON SEND FOR DELIVERY EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderCancelledAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER CANCELLED EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderReturnedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER RETURNED EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> orderDeliveredAction() {
        return context -> {
            logStateTransition(context, "ACTION ON ORDER DELIVERED EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    @Bean(name = "Order-Auto-Transition")
    public Action<OrderState, OrderEvent> autoTransition() {
        return context -> {
            System.out.println("(6) AUTO TRANSITIONING: == (StateMachineActions) ================================================= >>");
            // Get the State Machine from the context
            StateMachine<OrderState, OrderEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Order from the Extended State
                OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
                if(order != null) {
                    // Create Message for the Failure Event
                    Message mesg = MessageBuilder.withPayload(OrderEvent.AUTO_TRANSITION_EVENT)
                            .setHeader(OrderConstants.ORDER_ID_HEADER, order.getOrderId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                    logStateTransition(context, "AUTO TRANSITION EVENT SEND: ");
                }
            }
        };
    }

    /**
     * Log the State Transition Details
     * @param context
     * @param _msg
     */
    private void logStateTransition(StateContext<OrderState, OrderEvent> context, String _msg) {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("(3) STATE ACTION on EVENT  == (StateMachineActions) =========================================== >>");
        System.out.println("--------------------------------------------------------------------------------------------------");

        OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
        OrderState source = context.getSource().getId();
        OrderState target = context.getTarget().getId();
        OrderEvent event = context.getEvent();
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        String e = (event != null) ?  event.name() : "No-Event";
        String o = (order != null) ? order.getOrderId() : "No-Order-Found!";
        log.info("TRANSITIONING FROM [{}] TO ({}) based on EVENT = <{}>", s, t,e);
        log.info("{} for Order ID = {}",_msg,o);
    }
}
