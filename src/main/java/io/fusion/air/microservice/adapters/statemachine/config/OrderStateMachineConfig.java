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
import io.fusion.air.microservice.adapters.statemachine.core.OrderStateMachineErrorHandler;
import io.fusion.air.microservice.adapters.statemachine.core.OrderStateMachineGuards;
import io.fusion.air.microservice.domain.statemachine.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
// Java &  SLF4J
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

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
    private OrderStateMachineGuards guards;

    @Autowired
    private OrderStateMachineActions actions;

    @Autowired
    private OrderStateMachineErrorHandler errorHandler;

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
                .state(OrderState.IN_PROGRESS)                  // IN Progress State
                .state(OrderState.ERROR)                        // ERROR State
                .end(OrderState.ORDER_COMPLETED)                // Order Processing Completed
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

                    .state(OrderState.PAYMENT_DECLINED)          // :-( Sad Path
                    .state(OrderState.CANCELLED)                 // :-( Sad Path
                    .state(OrderState.RETURNED)                  // :-( Sad Path
                    .state(OrderState.DELIVERED)                 // :-) Happy Path

                    .stateEntry(OrderState.DELIVERED, actions.autoTransition())
                    .stateEntry(OrderState.RETURNED, actions.autoTransition())
                    .stateEntry(OrderState.CANCELLED, actions.autoTransition())
                ;
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
                    .first(OrderState.CREDIT_CHECKING, guards.creditCheckRequiredGuard(), actions.creditCheckAction())
                    .last(OrderState.PAYMENT_PROCESSING)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.PAYMENT_PROCESSING)
                    .event(OrderEvent.CREDIT_APPROVED_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.CREDIT_APPROVED)
                    .event(OrderEvent.CREDIT_APPROVED_EVENT)
                    .action(actions.creditApprovedAction())
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.CREDIT_DENIED)
                    .event(OrderEvent.CREDIT_DECLINED_EVENT)
                    .action(actions.creditDeniedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_DENIED).target(OrderState.CANCELLED)
                    .event(OrderEvent.ORDER_CANCELLED_EVENT)
                    .action(actions.orderCancelledAction())
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_APPROVED).target(OrderState.PAYMENT_PROCESSING)
                    .event(OrderEvent.PAYMENT_INIT_EVENT)
                    .action(actions.paymentInitAction())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_CONFIRMED)
                    .event(OrderEvent.PAYMENT_APPROVED_EVENT)
                    .action(actions.paymentApprovalAction())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_DECLINED)
                    .event(OrderEvent.PAYMENT_DECLINED_EVENT)
                    .action(actions.paymentDeclinedAction())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_DECLINED).target(OrderState.CANCELLED)
                    .event(OrderEvent.ORDER_CANCELLED_EVENT)
                    .action(actions.orderCancelledAction())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_CONFIRMED).target(OrderState.PACKING_IN_PROCESS)
                    .event(OrderEvent.ORDER_PACKAGE_EVENT)
                    .action(actions.orderPackagingAction())
                .and()
                .withExternal()
                    .source(OrderState.PACKING_IN_PROCESS).target(OrderState.READY_FOR_SHIPMENT)
                    .event(OrderEvent.ORDER_READY_TO_SHIP_EVENT)
                    .action(actions.orderReadyToShipAction())
                .and()
                .withExternal()
                    .source(OrderState.READY_FOR_SHIPMENT).target(OrderState.SHIPPED)
                    .event(OrderEvent.ORDER_SHIPPED_EVENT)
                    .action(actions.orderShippedAction())
                .and()
                .withExternal()
                    .source(OrderState.SHIPPED).target(OrderState.IN_TRANSIT)
                    .event(OrderEvent.ORDER_IN_TRANSIT_EVENT)
                    .action(actions.orderInTransitAction())
                .and()
                .withExternal()
                    .source(OrderState.IN_TRANSIT).target(OrderState.REACHED_DESTINATION)
                    .event(OrderEvent.SEND_FOR_DELIVERY_EVENT)
                    .action(actions.sendForDelivery())
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.DELIVERED)
                    .event(OrderEvent.ORDER_DELIVERED_EVENT)
                    .action(actions.orderDeliveredAction())
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.RETURNED)
                    .event(OrderEvent.ORDER_RETURNED_EVENT)
                    .action(actions.orderReturnedAction())
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.CANCELLED)
                    .event(OrderEvent.ORDER_CANCELLED_EVENT)
                    .action(actions.orderCancelledAction())
                .and()
                .withExternal()
                    .source(OrderState.DELIVERED).target(OrderState.ORDER_COMPLETED)
                    .event(OrderEvent.AUTO_TRANSITION_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.RETURNED).target(OrderState.ORDER_COMPLETED)
                    .event(OrderEvent.AUTO_TRANSITION_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CANCELLED).target(OrderState.ORDER_COMPLETED)
                    .event(OrderEvent.AUTO_TRANSITION_EVENT)

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
                .listener(stateMachineListener())
                .autoStartup(true);
    }

    /**
     * Logs the State Changes in the State Machine
     * @return
     */
    @Bean
    public StateMachineListener<OrderState, OrderEvent> stateMachineListener() {
        return new StateMachineListenerAdapter<OrderState, OrderEvent>() {
            /**
             * Log when the State changes.
             * @param from
             * @param to
             */
            @Override
            public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
                // System.out.println("STATE TRANSITION ================================================== >>");
                // logStateTransition(from, to);
            }

            /**
             * Handle the Errors throw by the Actions / Guards
             * @param stateMachine
             * @param exception
             */
            @Override
            public void stateMachineError(StateMachine<OrderState, OrderEvent> stateMachine, Exception exception) {
                System.out.println("STATE ERROR 1: ================================================== >>");

                // Capture the current state before transitioning to the error state
                OrderState errorSourceState = stateMachine.getState().getId();
                stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_SOURCE, errorSourceState);
                stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_MSG, exception.getMessage());
                // Log the exception and the source state
                log.error("<><><> Exception occurred during state transition from state: " + errorSourceState, exception);

                // Handle the Error and Send a Failure Event to State Machine
                // stateMachine.sendEvent(OrderEvent.FAILURE_EVENT);
            }

        };
    }

    /**
     * Log State Change
     * @param from
     * @param to
     */
    private void logStateTransition(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
        OrderState source = (from != null) ? from.getId() : null;
        OrderState target = (to != null) ? to.getId() : null;
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        log.info("LISTENER: STATE TRANSITIONING FROM [{}] TO ({})", s, t);
    }
}
