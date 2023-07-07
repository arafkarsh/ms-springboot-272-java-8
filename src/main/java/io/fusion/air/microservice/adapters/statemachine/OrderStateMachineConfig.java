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
// Custom
import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.statemachine.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
// Spring
import io.fusion.air.microservice.utils.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Spring State Machine
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
// Java &  SLF4J
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

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

                .choice(OrderState.CREDIT_CHOICE)        // Credit Check is a Choice based on Condition

                .state(OrderState.CREDIT_CHECKING)           // Credit Check Denied
                .state(OrderState.CREDIT_DENIED)           // Credit Check Denied
                .state(OrderState.CREDIT_APPROVED)         // Credit Check Approved

                .state(OrderState.PAYMENT_PROCESSING)      // Payment Process
                .state(OrderState.PAYMENT_CONFIRMED)

                .state(OrderState.PACKING_IN_PROCESS)      // Packing Process
                .state(OrderState.READY_FOR_SHIPMENT)      // Ready For Shipment
                .state(OrderState.SHIPPED)                 // Shipping Process
                .state(OrderState.IN_TRANSIT)

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
                    .source(OrderState.ORDER_INITIALIZED).target(OrderState.CREDIT_CHOICE)
                    .event(OrderEvent.CREDIT_CHECKING_EVENT)
                .and()
                .withChoice()
                    .source(OrderState.CREDIT_CHOICE)
                    .first(OrderState.CREDIT_CHECKING, creditCheckRequiredGuard())
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
                    .source(OrderState.PAYMENT_CONFIRMED).target(OrderState.SHIPPED)
                    .event(OrderEvent.SEND_FOR_DELIVERY_EVENT)
                .and()
                .withExternal()
                    .source(OrderState.SHIPPED).target(OrderState.DELIVERED)
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
            @Override
            public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
                log.info(String.format(">> State Changed From: %s >> To >> %s", from, to));
            }
        };
        return adapter;
    }


    // ==============================================================================================================
    // ORDER INITIALIZED
    // ==============================================================================================================

    @Bean
    public Guard<OrderState, OrderEvent> creditCheckRequiredGuard() {
        return context -> {
            // Following Method is Required if the OrderEntity is Send as a Message (sendMessage)
            // As the OrderEntity is Set as an Extended State following call is NOT Required.
            // OrderEntity order = (OrderEntity) context.getMessageHeader(OrderConstants.ORDER_HEADER);

            // Extract OrderEntity from the Extended State
            OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
            if(order != null) {
                log.info("Order = " + Utils.toJsonString(order));
                // Returns TRUE if the Order Value is greater than Rs. 1,00,000/-
                return order.getTotalOrderValue().compareTo(new BigDecimal("100000")) > 0;
            }
            // No CREDIT Check Required
            return false;
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
