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
package io.fusion.air.microservice.adapters.statemachine;

import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * Order State Machine
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Configuration
@EnableStateMachine
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    /**
     * Order Processing
     * State Machine - State Configuration
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderState.ORDER_INITIALIZED)     // Order Initialized
                .state(OrderState.CREDIT_CHECKING)         // Credit Check
                .state(OrderState.PAYMENT_PROCESSING)      // Payment Process
                .state(OrderState.PAYMENT_CONFIRMED)
                .state(OrderState.PACKING_IN_PROCESS)      // Packing Process
                .state(OrderState.READY_FOR_SHIPMENT)      // Ready For Shipment
                .state(OrderState.SHIPPED)                 // Shipping Process
                .state(OrderState.IN_TRANSIT)
                .state(OrderState.CREDIT_DENIED)           // :-( Sad Path
                .end(OrderState.PAYMENT_DECLINED)          // :-( Sad Path
                .end(OrderState.CANCELLED)                 // :-( Sad Path
                .end(OrderState.RETURNED)                  // :-( Sad Path
                .end(OrderState.DELIVERED);                // :-) Happy Path

    }

    /**
     * Order Processing
     * State Machine - Transition Configuration
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(OrderState.ORDER_INITIALIZED).target(OrderState.CREDIT_CHECKING)
                    .event(OrderEvent.CREDIT_CHECKING_EVENT)
                    .guard(creditCheckGuard())
                    .action(creditCheckAction())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_CONFIRMED)
                    .event(OrderEvent.CONFIRM_PAYMENT_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_CONFIRMED).target(OrderState.SHIPPED)
                    .event(OrderEvent.SEND_FOR_DELIVERY_EVENT);
    }

    // ==============================================================================================================
    // ORDER INITIALIZED
    // ==============================================================================================================

    @Bean
    public Guard<OrderState, OrderEvent> creditCheckGuard() {
        return context -> {
            // Add condition here
            return true;
        };
    }

    @Bean
    public Action<OrderState, OrderEvent> creditCheckAction() {
        return context -> {
            OrderState sourceState = context.getSource().getId();
            OrderState targetState = context.getTarget().getId();
            log.info("Transitioning from {} to {}", sourceState, targetState);
        };
    }

}
