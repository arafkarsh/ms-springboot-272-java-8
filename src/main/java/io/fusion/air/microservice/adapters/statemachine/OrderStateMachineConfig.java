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

import io.fusion.air.microservice.domain.statemachine.OrderEventTypes;
import io.fusion.air.microservice.domain.statemachine.OrderStates;

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
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEventTypes> {

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
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEventTypes> states) throws Exception {
        states
                .withStates()
                .initial(OrderStates.ORDER_INITIALIZED)
                .state(OrderStates.CREDIT_CHECKING)
                .state(OrderStates.PAYMENT_PROCESSING)
                .state(OrderStates.PAYMENT_CONFIRMED)
                .state(OrderStates.SHIPPED)
                .end(OrderStates.CANCELLED)
                .end(OrderStates.RETURNED)
                .end(OrderStates.DELIVERED);

    }

    /**
     * Order Processing
     * State Machine - Transition Configuration
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEventTypes> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(OrderStates.ORDER_INITIALIZED).target(OrderStates.CREDIT_CHECKING)
                    .event(OrderEventTypes.CREDIT_CHECKING_EVENT)
                    .guard(creditCheckGuard())
                    .action(creditCheckAction())
                .and()
                .withExternal()
                    .source(OrderStates.PAYMENT_PROCESSING).target(OrderStates.PAYMENT_CONFIRMED)
                    .event(OrderEventTypes.CONFIRM_PAYMENT_EVENT)
                .and()
                .withExternal()
                    .source(OrderStates.PAYMENT_CONFIRMED).target(OrderStates.SHIPPED)
                    .event(OrderEventTypes.SEND_FOR_DELIVERY_EVENT);
    }

    // ==============================================================================================================
    // ORDER INITIALIZED
    // ==============================================================================================================

    @Bean
    public Guard<OrderStates, OrderEventTypes> creditCheckGuard() {
        return context -> {
            // Add condition here
            return true;
        };
    }

    @Bean
    public Action<OrderStates, OrderEventTypes> creditCheckAction() {
        return context -> {
            OrderStates sourceState = context.getSource().getId();
            OrderStates targetState = context.getTarget().getId();
            log.info("Transitioning from {} to {}", sourceState, targetState);
        };
    }

}
