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
package io.fusion.air.microservice.adapters.statemachine.config;
// Custom
import io.fusion.air.microservice.adapters.statemachine.core.OrderStateMachineActions;
import io.fusion.air.microservice.adapters.statemachine.core.OrderStateMachineGuards;
import io.fusion.air.microservice.domain.statemachine.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
// Java &  SLF4J
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;
import org.springframework.statemachine.transition.Transition;

import java.math.BigDecimal;

/**
 * Order State Machine
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private OrderStateMachineGuards orderGuards;

    @Autowired
    private OrderStateMachineActions orderActions;

    /**
     * Order Processing
     * State Machine - State Configuration
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states
            .withStates()
                .initial(OrderState.ORDER_RECEIVED)            // Order RECEIVED
                .state(OrderState.IN_PROGRESS)
                .state(OrderState.ERROR)                        // ERROR State
                .end(OrderState.ORDER_COMPLETED)
                .and()
                .withStates()
                    .parent(OrderState.ORDER_RECEIVED)         // ROOT of All the States
                    .initial(OrderState.ORDER_INITIALIZED)     // Order Initialized
                    .choice(OrderState.CREDIT_CHOICE)          // Credit Check is a Choice based on Condition

                    .state(OrderState.CREDIT_CHECKING)         // Credit Check Denied
                    .state(OrderState.CREDIT_DENIED)           // Credit Check Denied
                    .state(OrderState.CREDIT_APPROVED)         // Credit Check Approved

                    .state(OrderState.PAYMENT_PROCESSING)      // Payment Process
                    .state(OrderState.PAYMENT_CONFIRMED)        // Payment Confirmed

                    .state(OrderState.PACKING_IN_PROCESS)      // Packing Process
                    .state(OrderState.READY_FOR_SHIPMENT)      // Ready For Shipment

                    .state(OrderState.SHIPPED)                 // Shipping Process
                    .state(OrderState.IN_TRANSIT)              // In Transit
                    .state(OrderState.REACHED_DESTINATION)     // Order Reached the Destination

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
                    .source(OrderState.ORDER_RECEIVED).target(OrderState.CREDIT_CHOICE)
                    .event(OrderEvent.CREDIT_CHECKING_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.ORDER_INITIALIZED).target(OrderState.CREDIT_CHOICE)
                    .event(OrderEvent.CREDIT_CHECKING_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.ORDER_RECEIVED).target(OrderState.ERROR)
                    .event(OrderEvent.FAILURE_EVENT)
                .and()
                .withChoice()
                    .source(OrderState.CREDIT_CHOICE)
                    .first(OrderState.CREDIT_CHECKING, orderGuards.creditCheckRequiredGuard(), orderActions.creditCheckAction())
                    .last(OrderState.PAYMENT_PROCESSING)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.PAYMENT_PROCESSING)
                    .event(OrderEvent.CREDIT_APPROVED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.CREDIT_APPROVED)
                    .event(OrderEvent.CREDIT_APPROVED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.CREDIT_DENIED)
                    .event(OrderEvent.CREDIT_DECLINED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_DENIED).target(OrderState.CANCELLED)
                    .event(OrderEvent.ORDER_CANCELLED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_APPROVED).target(OrderState.PAYMENT_PROCESSING)
                    .event(OrderEvent.PAYMENT_INIT_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_CONFIRMED)
                    .event(OrderEvent.PAYMENT_APPROVED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_DECLINED)
                    .event(OrderEvent.PAYMENT_DECLINED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_DECLINED).target(OrderState.CANCELLED)
                    .event(OrderEvent.ORDER_CANCELLED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_CONFIRMED).target(OrderState.PACKING_IN_PROCESS)
                    .event(OrderEvent.ORDER_PACKAGE_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.PACKING_IN_PROCESS).target(OrderState.READY_FOR_SHIPMENT)
                    .event(OrderEvent.ORDER_READY_TO_SHIP_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.READY_FOR_SHIPMENT).target(OrderState.SHIPPED)
                    .event(OrderEvent.ORDER_SHIPPED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.SHIPPED).target(OrderState.IN_TRANSIT)
                    .event(OrderEvent.ORDER_IN_TRANSIT_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.IN_TRANSIT).target(OrderState.REACHED_DESTINATION)
                    .event(OrderEvent.SEND_FOR_DELIVERY_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.DELIVERED)
                    .event(OrderEvent.ORDER_DELIVERED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.SHIPPED).target(OrderState.RETURNED)
                    .event(OrderEvent.ORDER_RETURNED_EVENT)
        ;
    }

    /**
     * Configuration for the Entire State Machine
     * Add a Listener to Keep Track of State Changes
     *
     * @param config
     * @throws Exception
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        config
                .withConfiguration()
                .listener(getSMListenerAdapter())
                .autoStartup(true);
    }

    /**
     * Logs the State Changes in the State Machine
     * @return
     */
    private StateMachineListenerAdapter<OrderState, OrderEvent> getSMListenerAdapter() {
        StateMachineListenerAdapter<OrderState, OrderEvent> adapter = new StateMachineListenerAdapter<OrderState, OrderEvent>() {
            /**
             * Log when the State changes.
             * @param from
             * @param to
             */
            @Override
            public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
                log.info(String.format(">> State Changed From: %s >> To >> %s", from, to));
            }

            /**
             * Handle the Errors throw by the Actions / Guards
             * @param stateMachine
             * @param exception
             */
            public void stateMachineError(StateMachine<OrderState, OrderEvent> stateMachine, Exception exception) {
                // Capture the current state before transitioning to the error state
                OrderState errorSourceState = stateMachine.getState().getId();
                stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_SOURCE, errorSourceState);
                stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_MSG, exception.getMessage());
                // Log the exception and the source state
                log.error("Exception occurred during state transition from state: " + errorSourceState, exception);

                // Handle the Error and Send a Failure Event to State Machine
                stateMachine.sendEvent(OrderEvent.FAILURE_EVENT);
            }

        };
        return adapter;
    }
}
