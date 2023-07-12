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
     * Reservation Processing
     * State Machine - State Configuration
     *
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<ReservationState, ReservationEvent> states) throws Exception {
        states
            .withStates()
                .initial(ReservationState.RESERVATION_REQUEST_RECEIVED)
                .region("Reservation Process")
                .choice(ReservationState.RESERVATION_REQUIRED)
                .state(ReservationState.RESERVATION_INITIALIZED)
                .state(ReservationState.RESERVATION_INVALID)
                .state(ReservationState.IN_PROGRESS)
                .state(ReservationState.ERROR)
                .end(ReservationState.RESERVATION_COMPLETED)
                .and()
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.HOTEL_BOOKING_INIT)
                    .choice(ReservationState.HOTEL_CHOICE)
                    .state(ReservationState.HOTEL_BOOKING_REQUEST)
                    .state(ReservationState.HOTEL_BOOKING_CONFIRMED)
                    .state(ReservationState.HOTEL_BOOKING_DECLINED)
                    .state(ReservationState.HOTEL_BOOKING_ROLLBACK)
                    .state(ReservationState.HOTEL_BOOKING_CANCELLED)
                .and()
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.RENTAL_BOOKING_INIT)
                    .choice(ReservationState.RENTAL_CHOICE)
                    .state(ReservationState.RENTAL_BOOKING_REQUEST)
                    .state(ReservationState.RENTAL_BOOKING_CONFIRMED)
                    .state(ReservationState.RENTAL_BOOKING_DECLINED)
                    .state(ReservationState.RENTAL_BOOKING_ROLLBACK)
                    .state(ReservationState.RENTAL_BOOKING_CANCELLED)
                .and()
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.FLIGHT_BOOKING_INIT)
                    .choice(ReservationState.FLIGHT_CHOICE)
                    .state(ReservationState.FLIGHT_BOOKING_REQUEST)
                    .state(ReservationState.FLIGHT_BOOKING_CONFIRMED)
                    .state(ReservationState.FLIGHT_BOOKING_DECLINED)
                    .state(ReservationState.FLIGHT_BOOKING_ROLLBACK)
                    .state(ReservationState.FLIGHT_BOOKING_CANCELLED)
                .and()
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.PAYMENT_REQUEST_INIT)
                    .state(ReservationState.PAYMENT_APPROVED)
                    .state(ReservationState.PAYMENT_DECLINED)
                    .state(ReservationState.SEND_INVOICE_START)
                    .state(ReservationState.SEND_TRAVEL_DETAILS_START)
                    .state(ReservationState.TRIP_CONFIRMED)

        ;
    }

    /**
     * Reservation Processing
     * State Machine - Transition Configuration
     *
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<ReservationState, ReservationEvent> transitions) throws Exception {
        transitions
            .withExternal()
                .source(ReservationState.RESERVATION_REQUEST_RECEIVED).target(ReservationState.RESERVATION_REQUIRED)
                .event(ReservationEvent.RESERVATION_VALIDATION_EVENT)
            .and()
            .withExternal()
                .source(ReservationState.IN_PROGRESS).target(ReservationState.ERROR)
                .event(ReservationEvent.FAILURE_EVENT)
            .and()
            .withChoice()
                .source(ReservationState.RESERVATION_REQUIRED)
                .first(ReservationState.RESERVATION_INITIALIZED, guards.checkOrderValidity())
                .last(ReservationState.RESERVATION_INVALID)
            .and()
            .withExternal()
                .source(ReservationState.RESERVATION_INITIALIZED).target(ReservationState.IN_PROGRESS)
                .event(ReservationEvent.RESERVATION_INIT_EVENT)
            .and()
            .withExternal()
                .source(ReservationState.IN_PROGRESS).target(ReservationState.HOTEL_CHOICE)
                .event(ReservationEvent.HOTEL_BOOKING_REQUEST_EVENT)
            .and()
             // Hotel
            .withChoice()
                .source(ReservationState.HOTEL_CHOICE)
                .first(ReservationState.HOTEL_BOOKING_REQUEST, guards.doHotelBooking())
                .last(ReservationState.RENTAL_CHOICE)
                .and()
                .withExternal()
                    .source(ReservationState.HOTEL_BOOKING_REQUEST).target(ReservationState.HOTEL_BOOKING_CONFIRMED)
                    .event(ReservationEvent.HOTEL_BOOKING_CONFIRMED_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.HOTEL_BOOKING_REQUEST).target(ReservationState.HOTEL_BOOKING_DECLINED)
                    .event(ReservationEvent.HOTEL_BOOKING_DECLINED_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.HOTEL_BOOKING_ROLLBACK).target(ReservationState.HOTEL_BOOKING_CANCELLED)
                    .event(ReservationEvent.HOTEL_BOOKING_ROLLBACK_EVENT)
            .and()
            .withExternal()
                .source(ReservationState.HOTEL_BOOKING_CONFIRMED).target(ReservationState.RENTAL_CHOICE)
                .event(ReservationEvent.RENTAL_BOOKING_REQUEST_EVENT)
            .and()
            // Rental
            .withChoice()
                .source(ReservationState.RENTAL_CHOICE)
                .first(ReservationState.RENTAL_BOOKING_REQUEST, guards.doRentalBooking())
                .last(ReservationState.FLIGHT_CHOICE)
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_REQUEST).target(ReservationState.RENTAL_BOOKING_CONFIRMED)
                    .event(ReservationEvent.RENTAL_BOOKING_CONFIRMED_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_REQUEST).target(ReservationState.RENTAL_BOOKING_DECLINED)
                    .event(ReservationEvent.RENTAL_BOOKING_DECLINED_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_ROLLBACK).target(ReservationState.RENTAL_BOOKING_CANCELLED)
                    .event(ReservationEvent.RENTAL_BOOKING_ROLLBACK_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_CONFIRMED).target(ReservationState.FLIGHT_CHOICE)
                    .event(ReservationEvent.FLIGHT_BOOKING_REQUEST_EVENT)
            .and()
            // Flight
            .withChoice()
                .source(ReservationState.FLIGHT_CHOICE)
                .first(ReservationState.FLIGHT_BOOKING_REQUEST, guards.doFlightBooking())
                .last(ReservationState.PAYMENT_REQUEST_INIT)
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_REQUEST).target(ReservationState.FLIGHT_BOOKING_CONFIRMED)
                    .event(ReservationEvent.FLIGHT_BOOKING_CONFIRMED_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_REQUEST).target(ReservationState.FLIGHT_BOOKING_DECLINED)
                    .event(ReservationEvent.FLIGHT_BOOKING_DECLINED_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_ROLLBACK).target(ReservationState.FLIGHT_BOOKING_CANCELLED)
                    .event(ReservationEvent.FLIGHT_BOOKING_ROLLBACK_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_CONFIRMED).target(ReservationState.PAYMENT_REQUEST_INIT)
                    .event(ReservationEvent.PAYMENT_REQUEST_EVENT)
                .and()
            //  Payment
            .withExternal()
                .source(ReservationState.PAYMENT_REQUEST_INIT).target(ReservationState.PAYMENT_APPROVED)
                .event(ReservationEvent.PAYMENT_CONFIRMED_EVENT)
                .and()
                .withExternal()
                .source(ReservationState.PAYMENT_REQUEST_INIT).target(ReservationState.PAYMENT_DECLINED)
                .event(ReservationEvent.PAYMENT_DECLINED_EVENT)
                .and()
                .withExternal()
                .source(ReservationState.PAYMENT_APPROVED).target(ReservationState.SEND_INVOICE_START)
                .event(ReservationEvent.SEND_INVOICE_EVENT)
                .and()
                .withExternal()
                .source(ReservationState.SEND_INVOICE_START).target(ReservationState.SEND_TRAVEL_DETAILS_START)
                .event(ReservationEvent.SEND_TRAVEL_DETAILS_EVENT)
                .and()
                .withExternal()
                .source(ReservationState.SEND_TRAVEL_DETAILS_START).target(ReservationState.TRIP_CONFIRMED)
                .event(ReservationEvent.AUTO_TRANSITION_EVENT)
                .source(ReservationState.PAYMENT_DECLINED).target(ReservationState.TRIP_CANCELLED)
                .event(ReservationEvent.AUTO_TRANSITION_EVENT)
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
    public void configure(StateMachineConfigurationConfigurer<ReservationState, ReservationEvent> config) throws Exception {
        config
                .withConfiguration()
                .machineId("reservationProcessingStateMachine")
                .listener(stateMachineListener)
                .autoStartup(true);

    }
}
