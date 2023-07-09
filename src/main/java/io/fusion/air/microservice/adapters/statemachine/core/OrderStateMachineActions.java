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
import io.fusion.air.microservice.domain.statemachine.OrderNotes;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
            try {
                OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
                OrderState sourceState = context.getSource().getId();
                OrderState targetState = context.getTarget().getId();
                log.info("Transitioning FROM {} TO {}", sourceState, targetState);
                log.info("Send CREDIT CHECK EVENT for Order ID = "+order.getOrderId());
            } catch (Exception e) {

            }
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean
    public Action<OrderState, OrderEvent> creditDeniedAction() {
        return context -> {
            OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
            OrderState sourceState = context.getSource().getId();
            OrderState targetState = context.getTarget().getId();
            log.info("Transitioning FROM {} TO {}", sourceState, targetState);
            log.info("To Demo Exception Handling: in Credit Denied: Order ID = "+order.getOrderId());
            throw new BusinessServiceException("Error in Credit Denied State: Target State = "+targetState.name());
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> handleError() {
        return context -> {
            System.out.println("STATE ERROR 2: ================================================== >>");
            // Extract Order States and Events
            OrderState sourceState = context.getSource().getId();
            OrderState targetState = context.getTarget().getId();
            OrderEvent event = context.getEvent();
            String s = (sourceState != null) ? sourceState.name() : "No-Source";
            String t = (targetState != null) ? targetState.name() : "No-Target";
            String e = (event != null) ?  event.name() : "No-Event";

            // Order Notes Object to capture errors
            OrderNotes error = new OrderNotes(s,t,e, "", context.getException().getMessage());
            // Store the Order Notes in Extended State
            StateMachine<OrderState, OrderEvent> stateMachine = context.getStateMachine();
            stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_OBJECT, error);
            // Extract Order ID from the Extended State
            OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
            log.info("HANDLE ERROR in Target State:{} Order ID = {}",t, order.getOrderId());

            // Create Message for the Failure Event
            Message mesg = MessageBuilder.withPayload(OrderEvent.FAILURE_EVENT)
                    .setHeader(OrderConstants.ORDER_ID_HEADER, order.getOrderId())
                    .build();
            // Send Event to the State Machine
            stateMachine.sendEvent(mesg);

        };
    }
}
