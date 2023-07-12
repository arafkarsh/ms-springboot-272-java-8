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
package io.fusion.air.microservice.adapters.statemachine.order.config;
// Custom
import io.fusion.air.microservice.adapters.statemachine.order.core.OrderStateMachineActions;
import io.fusion.air.microservice.adapters.statemachine.order.core.OrderStateMachineErrorHandler;
import io.fusion.air.microservice.adapters.statemachine.order.core.OrderStateMachineGuards;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
// Spring State Machine
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
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
@EnableStateMachineFactory(name = "Order-State-Machine-Factory")
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private OrderStateMachineGuards guards;

    @Autowired
    private OrderStateMachineActions actions;

    @Autowired
    private OrderStateMachineErrorHandler errorHandler;

    @Autowired
    private OrderStateMachineListenerAdapter stateMachineListener;

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
                .initial(OrderState.ORDER_RECEIVED)                         // Order RECEIVED
                .region("Order Processing")

                .state(OrderState.IN_PROGRESS)                              // IN Progress State
                .state(OrderState.ERROR)                                    // ERROR State
                .end(OrderState.ORDER_COMPLETED)                            // Order Processing Completed
                .and()
                .withStates()
                    .parent(OrderState.ORDER_RECEIVED)                      // PARENT of All the States
                    .initial(OrderState.ORDER_INITIALIZED)                  // Order Initialized
                    .region("Payment")

                    .choice(OrderState.CREDIT_CHOICE)                       // Credit Check is a Choice based on Condition

                    .state(OrderState.CREDIT_CHECKING)                      // Credit Check Denied
                    .state(OrderState.CREDIT_DENIED)                        // Credit Check Denied
                    .state(OrderState.CREDIT_APPROVED)                      // Credit Check Approved

                    .state(OrderState.PAYMENT_PROCESSING)                   // Payment Process
                    .state(OrderState.PAYMENT_CONFIRMED)                    // Payment Confirmed
                    .state(OrderState.PAYMENT_DECLINED)                     // :-( Sad Path

                    .fork(OrderState.PACKING_FORK)                          // FORK - Parallel Tasks
                    .join(OrderState.READY_TO_SHIP_JOIN)                    // JOIN the Parallel Tasks
                    .state(OrderState.SHIPPED)                              // Shipped State
                    .and()

                    .withStates()
                        .parent(OrderState.PACKING_FORK)                    // Parent State - FORK
                        .initial(OrderState.ORDER_PACKAGING_START)          // Initial State - for Packaging
                        .state(OrderState.ORDER_PACKAGING_DONE)             // Packaging Done
                        .region("Packing")                               // Define Region
                        .end(OrderState.ORDER_PACKAGING_DONE)               // End of Child Region
                    .and()

                    .withStates()
                        .parent(OrderState.PACKING_FORK)                    // Parent State - FORK
                        .initial(OrderState.SEND_BILL_START)                // Send Invoice
                        .state(OrderState.SEND_BILL_DONE)                   // Invoice is Done
                        .region("Notification")                           // Notification Region
                        .end(OrderState.SEND_BILL_DONE)                     // End of Child Region
                    .and()

                    .withStates()
                        .parent(OrderState.SHIPPED)                         // Parent State - Shipped
                        .initial(OrderState.IN_TRANSIT)                     // In Transit
                        .state(OrderState.REACHED_DESTINATION)              // Order Reached the Destination
                        .region("Shipping")                             // Shipping Region

                        .stateEntry(OrderState.DELIVERED, actions.autoTransition())
                        .stateEntry(OrderState.RETURNED, actions.autoTransition())
                        .stateEntry(OrderState.CANCELLED, actions.autoTransition())

                        .state(OrderState.CANCELLED)                        // :-( Sad Path
                        .state(OrderState.RETURNED)                         // :-( Sad Path
                        .state(OrderState.DELIVERED)                        // :-) Happy Path
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
                    .source(OrderState.ORDER_RECEIVED).target(OrderState.CREDIT_CHOICE).event(OrderEvent.CREDIT_CHECKING_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.ORDER_INITIALIZED).target(OrderState.CREDIT_CHOICE).event(OrderEvent.CREDIT_CHECKING_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.ORDER_RECEIVED).target(OrderState.ERROR).event(OrderEvent.FAILURE_EVENT)
                .and()
                .withChoice()
                    .source(OrderState.CREDIT_CHOICE)
                    .first(OrderState.CREDIT_CHECKING, guards.creditCheckRequiredGuard(), actions.creditCheckAction())
                    .last(OrderState.PAYMENT_PROCESSING)
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.CREDIT_APPROVED).event(OrderEvent.CREDIT_APPROVED_EVENT)
                    .action(actions.creditApprovedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_CHECKING).target(OrderState.CREDIT_DENIED).event(OrderEvent.CREDIT_DECLINED_EVENT)
                    .action(actions.creditDeniedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_DENIED).target(OrderState.CANCELLED).event(OrderEvent.ORDER_CANCELLED_EVENT)
                    .action(actions.orderCancelledAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.CREDIT_APPROVED).target(OrderState.PAYMENT_PROCESSING).event(OrderEvent.PAYMENT_INIT_EVENT)
                    .action(actions.paymentInitAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_CONFIRMED).event(OrderEvent.PAYMENT_APPROVED_EVENT)
                    .action(actions.paymentApprovalAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_PROCESSING).target(OrderState.PAYMENT_DECLINED).event(OrderEvent.PAYMENT_DECLINED_EVENT)
                    .action(actions.paymentDeclinedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_DECLINED).target(OrderState.CANCELLED).event(OrderEvent.ORDER_CANCELLED_EVENT)
                    .action(actions.orderCancelledAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.PAYMENT_CONFIRMED).target(OrderState.PACKING_FORK).event(OrderEvent.PACKAGE_FORK_EVENT)
                    .action(actions.orderPackagingAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.ORDER_PACKAGING_START).target(OrderState.ORDER_PACKAGING_DONE).event(OrderEvent.PACKAGE_EVENT)
                    .action(actions.orderReadyToShipAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.SEND_BILL_START).target(OrderState.SEND_BILL_DONE).event(OrderEvent.ORDER_SEND_BILL_EVENT)
                    .action(actions.sendBillAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.READY_TO_SHIP_JOIN).target(OrderState.SHIPPED).event(OrderEvent.ORDER_SHIPPED_EVENT)
                    .action(actions.orderShippedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.SHIPPED).target(OrderState.IN_TRANSIT).event(OrderEvent.ORDER_IN_TRANSIT_EVENT)
                    .action(actions.orderInTransitAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.IN_TRANSIT).target(OrderState.REACHED_DESTINATION).event(OrderEvent.SEND_FOR_DELIVERY_EVENT)
                    .action(actions.sendForDelivery(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.DELIVERED).event(OrderEvent.ORDER_DELIVERED_EVENT)
                    .action(actions.orderDeliveredAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.RETURNED).event(OrderEvent.ORDER_RETURNED_EVENT)
                    .action(actions.orderReturnedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.REACHED_DESTINATION).target(OrderState.CANCELLED).event(OrderEvent.ORDER_CANCELLED_EVENT)
                    .action(actions.orderCancelledAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(OrderState.DELIVERED).target(OrderState.ORDER_COMPLETED).event(OrderEvent.AUTO_TRANSITION_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.RETURNED).target(OrderState.ORDER_COMPLETED).event(OrderEvent.AUTO_TRANSITION_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.CANCELLED).target(OrderState.ORDER_COMPLETED).event(OrderEvent.AUTO_TRANSITION_EVENT)
                .and()
                .withFork()
                    .source(OrderState.PACKING_FORK)
                    .target(OrderState.ORDER_PACKAGING_START)
                    .target(OrderState.SEND_BILL_START)
                .and()
                .withJoin()
                    .source(OrderState.ORDER_PACKAGING_DONE)
                    .source(OrderState.SEND_BILL_DONE)
                    .target(OrderState.READY_TO_SHIP_JOIN)
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
                .machineId("orderProcessingStateMachine")
                .listener(stateMachineListener)
                .autoStartup(true);

    }
}
