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

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;


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
                .initial(OrderStates.ORDER_PLACED)
                .state(OrderStates.CREDIT_CHECKING)
                .state(OrderStates.PAYMENT_PROCESSING)
                .state(OrderStates.PAYMENT_CONFIRMED)
                .state(OrderStates.SHIPPED)
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
                .source(OrderStates.ORDER_PLACED).target(OrderStates.PAYMENT_CONFIRMED).event(OrderEventTypes.CONFIRM_PAYMENT_EVENT)
                .and()
                .withExternal()
                .source(OrderStates.PAYMENT_CONFIRMED).target(OrderStates.SHIPPED).event(OrderEventTypes.SEND_FOR_DELIVERY_EVENT);
    }
}
