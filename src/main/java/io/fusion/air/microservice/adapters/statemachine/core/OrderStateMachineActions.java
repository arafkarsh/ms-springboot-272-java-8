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
package io.fusion.air.microservice.adapters.statemachine.core;
// Custom
import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.exceptions.BusinessServiceException;
import io.fusion.air.microservice.domain.statemachine.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateContext;
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
            logStateTransition(context, "Send CREDIT CHECK EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean
    public Action<OrderState, OrderEvent> creditDeniedAction() {
        return context -> {
            logStateTransition(context, "To Demo Exception Handling: in Credit Denied:");
            // Add Business Logic to Handle Event
            // ...
            throw new BusinessServiceException("Error in Credit Denied State: Target State = "+context.getTarget().getId().name());
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> paymentApprovalAction() {
        return context -> {
            logStateTransition(context, "Send PAYMENT APPROVAL EVENT");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    /**
     * Log the State Transition Details
     * @param context
     * @param _msg
     */
    private void logStateTransition(StateContext<OrderState, OrderEvent> context, String _msg) {
        OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
        OrderState source = context.getSource().getId();
        OrderState target = context.getTarget().getId();
        OrderEvent event = context.getEvent();
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        String e = (event != null) ?  event.name() : "No-Event";
        String o = (order != null) ? order.getOrderId() : "No-Order-Found!";
        log.info("TRANSITIONING FROM {} TO {} based on EVENT = {}", s, t,e);
        log.info("{} for Order ID = {}",_msg,o);
    }
}
