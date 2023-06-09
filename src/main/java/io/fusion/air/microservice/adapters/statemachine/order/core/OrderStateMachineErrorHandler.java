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
import io.fusion.air.microservice.domain.statemachine.order.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderNotes;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
// Java
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order State Machine Error Handler
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Component
public class OrderStateMachineErrorHandler {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Bean(name = "Order-Error-Handler")
    public Action<OrderState, OrderEvent> handleError() {
        return context -> {
            System.out.println("STATE ERROR 2: ================================================== >>");
            // Order Notes Object to capture errors
            OrderNotes error = createOrderNotes(context);
            // Get the State Machine from the context
            StateMachine<OrderState, OrderEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Store the Order Notes in Extended State
                stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_OBJECT, error);
                // Extract Order from the Extended State
                OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
                if(order != null) {
                    // Create Message for the Failure Event
                    Message mesg = MessageBuilder.withPayload(OrderEvent.FAILURE_EVENT)
                            .setHeader(OrderConstants.ORDER_ID_HEADER, order.getOrderId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                }
            }
        };
    }

    /**
     * Create Order Notes from the Context
     * @param context
     */
    private OrderNotes createOrderNotes(StateContext<OrderState, OrderEvent> context) {
        // Extract Order States and Events
        OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
        OrderState source = context.getSource().getId();
        OrderState target = context.getTarget().getId();
        OrderEvent event = context.getEvent();
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        String e = (event != null) ?  event.name() : "No-Event";
        String o = (order != null) ? order.getOrderId() : "No-Order-Found!";
        String errorMessage = (context.getException() != null) ? context.getException().getMessage() : "";
        log.info("TRANSITIONING FAILED: FROM [{}] TO ({}) based on EVENT = <{}>", s, t,e);
        log.info("{} for Order ID = {}",errorMessage,o);
        return new OrderNotes(s,t,e, "", errorMessage);
    }
}
