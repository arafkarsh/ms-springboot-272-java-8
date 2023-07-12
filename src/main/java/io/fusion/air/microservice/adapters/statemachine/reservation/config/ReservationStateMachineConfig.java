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
package io.fusion.air.microservice.adapters.statemachine.reservation.config;
// Custom
import io.fusion.air.microservice.adapters.statemachine.reservation.core.ReservationStateMachineActions;
import io.fusion.air.microservice.adapters.statemachine.reservation.core.ReservationStateMachineErrorHandler;
import io.fusion.air.microservice.adapters.statemachine.reservation.core.ReservationStateMachineGuards;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
// Spring State Machine
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
// Java
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

/**
 * Reservation State Machine
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Configuration
@EnableStateMachineFactory(name = "Reservation-State-Machine-Factory")
public class ReservationStateMachineConfig extends EnumStateMachineConfigurerAdapter<ReservationState, ReservationEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private ReservationStateMachineGuards guards;

    @Autowired
    private ReservationStateMachineActions actions;

    @Autowired
    private ReservationStateMachineErrorHandler errorHandler;

    @Autowired
    private ReservationStateMachineListenerAdapter stateMachineListener;

    /**
     * Order Processing
     * State Machine - State Configuration
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<ReservationState, ReservationEvent> states) throws Exception {
        }

    /**
     * Order Processing
     * State Machine - Transition Configuration
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<ReservationState, ReservationEvent> transitions) throws Exception {

    }

    /**
     * Configuration for the Entire State Machine
     * Add a Listener to Keep Track of State Changes
     *
     * @param config
     * @throws Exception
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<ReservationState, ReservationEvent> config) throws Exception {
        config
                .withConfiguration()
                .machineId("reservationProcessingStateMachine")
                .listener(stateMachineListener)
                .autoStartup(true);

    }
}
