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
import io.fusion.air.microservice.adapters.statemachine.reservation.core.ReservationStateMachineRollbackActions;
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
    private ReservationStateMachineRollbackActions rollbackActions;

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

                // Distributed Tx Rollback
                .state(ReservationState.ROLLBACK)
                .state(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                .state(ReservationState.ROLLBACK_COMPLETED)
                // .stateEntry(ReservationState.ROLLBACK, actions.autoTransition())
                .state(ReservationState.RESERVATION_TERMINATED)

                .end(ReservationState.RESERVATION_COMPLETED)
                .and()

                // Hotel Booking
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.HOTEL_BOOKING_INIT)
                    .choice(ReservationState.HOTEL_CHOICE)
                    .state(ReservationState.HOTEL_BOOKING_REQUEST)
                    // .region("Hotel Booking")
                    .state(ReservationState.HOTEL_BOOKING_CONFIRMED)
                    .state(ReservationState.HOTEL_BOOKING_DECLINED)

                    // Distributed Tx Rollback
                    .state(ReservationState.HOTEL_BOOKING_ROLLBACK)
                    .stateEntry(ReservationState.HOTEL_BOOKING_ROLLBACK, rollbackActions.rollBackAction())
                    .state(ReservationState.HOTEL_BOOKING_CANCELLED)
                    .stateEntry(ReservationState.HOTEL_BOOKING_CANCELLED, rollbackActions.rollBackAction())
                .and()

                // Rental Booking
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.RENTAL_BOOKING_INIT)
                    .choice(ReservationState.RENTAL_CHOICE)
                    .state(ReservationState.RENTAL_BOOKING_REQUEST)
                    // .region("Rental Booking")
                    .state(ReservationState.RENTAL_BOOKING_CONFIRMED)
                    .state(ReservationState.RENTAL_BOOKING_DECLINED)

                    // Distributed Tx Rollback
                    .state(ReservationState.RENTAL_BOOKING_ROLLBACK)
                    .stateEntry(ReservationState.RENTAL_BOOKING_ROLLBACK, rollbackActions.rollBackAction())
                    .state(ReservationState.RENTAL_BOOKING_CANCELLED)
                    .stateEntry(ReservationState.RENTAL_BOOKING_CANCELLED, rollbackActions.rollBackAction())
                .and()

                // Flight Booking
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.FLIGHT_BOOKING_INIT)
                    .choice(ReservationState.FLIGHT_CHOICE)
                    .state(ReservationState.FLIGHT_BOOKING_REQUEST)
                    // .region("Flight Booking")
                    .state(ReservationState.FLIGHT_BOOKING_CONFIRMED)
                    .state(ReservationState.FLIGHT_BOOKING_DECLINED)

                    // Distributed Tx Rollback
                    .state(ReservationState.FLIGHT_BOOKING_ROLLBACK)
                    .stateEntry(ReservationState.FLIGHT_BOOKING_ROLLBACK, rollbackActions.rollBackAction())
                    .state(ReservationState.FLIGHT_BOOKING_CANCELLED)
                    .stateEntry(ReservationState.FLIGHT_BOOKING_CANCELLED, rollbackActions.rollBackAction())
                .and()

                // Payment and Invoice
                .withStates()
                    .parent(ReservationState.IN_PROGRESS)
                    .initial(ReservationState.PAYMENT_REQUEST_INIT)
                    .state(ReservationState.PAYMENT_APPROVED)
                    // .region("Payment & Invoice")
                    .state(ReservationState.PAYMENT_DECLINED)
                    .state(ReservationState.SEND_INVOICE)
                    .state(ReservationState.SEND_TRAVEL_DETAILS)

                    .state(ReservationState.TRIP_CONFIRMED)
                    .state(ReservationState.TRIP_CANCELLED)
                    // Auto Transition Starts to complete the Reservation
                    .stateEntry(ReservationState.TRIP_CONFIRMED, actions.autoTransition())
                    // Rollback Starts with Trip Cancellation
                    .stateEntry(ReservationState.TRIP_CANCELLED, rollbackActions.startRollbackAction())
                    .stateEntry(ReservationState.ROLLBACK_IN_PROGRESS, rollbackActions.startRollbackProcess())
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
                .first(ReservationState.RESERVATION_INITIALIZED, guards.checkReservationValidity())
                .last(ReservationState.RESERVATION_INVALID)
            .and()
            .withExternal()
                .source(ReservationState.RESERVATION_INITIALIZED).target(ReservationState.IN_PROGRESS)
                .event(ReservationEvent.RESERVATION_INIT_EVENT)
            .and()

            // Handling In-Progress with Various Services
            .withExternal()
                .source(ReservationState.IN_PROGRESS).target(ReservationState.HOTEL_CHOICE)
                .event(ReservationEvent.HOTEL_BOOKING_REQUEST_EVENT)
                .action(actions.hotelReservationRequestAction(), errorHandler.handleError())
            .and()
            .withExternal()
                .source(ReservationState.IN_PROGRESS).target(ReservationState.RENTAL_CHOICE)
                .event(ReservationEvent.RENTAL_BOOKING_REQUEST_EVENT)
                .action(actions.rentalReservationRequestAction(), errorHandler.handleError())
            .and()
            .withExternal()
                .source(ReservationState.IN_PROGRESS).target(ReservationState.FLIGHT_CHOICE)
                .event(ReservationEvent.FLIGHT_BOOKING_REQUEST_EVENT)
                .action(actions.flightReservationRequestAction(), errorHandler.handleError())
                .and()
            .withExternal()
                .source(ReservationState.IN_PROGRESS).target(ReservationState.PAYMENT_REQUEST_INIT)
                .event(ReservationEvent.PAYMENT_REQUEST_EVENT)
                .action(actions.paymentRequestAction(), errorHandler.handleError())
                .and()

             // Hotel
            .withChoice()
                .source(ReservationState.HOTEL_CHOICE)
                .first(ReservationState.HOTEL_BOOKING_REQUEST, guards.doHotelBooking())
                .last(ReservationState.IN_PROGRESS)
                .and()
                .withExternal()
                    .source(ReservationState.HOTEL_BOOKING_REQUEST).target(ReservationState.HOTEL_BOOKING_CONFIRMED)
                    .event(ReservationEvent.HOTEL_BOOKING_CONFIRMED_EVENT)
                    .action(actions.hotelReservationConfirmedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.HOTEL_BOOKING_REQUEST).target(ReservationState.HOTEL_BOOKING_DECLINED)
                    .event(ReservationEvent.HOTEL_BOOKING_DECLINED_EVENT)
                    .action(actions.hotelReservationDeclinedAction(), errorHandler.handleError())
                .and()
            .withExternal()
                .source(ReservationState.HOTEL_BOOKING_CONFIRMED).target(ReservationState.IN_PROGRESS)
                .event(ReservationEvent.HOTEL_BOOKING_COMPLETED_EVENT)
                .action(actions.hotelReservationCompletedAction(), errorHandler.handleError())
                .and()

            // Rental
            .withChoice()
                .source(ReservationState.RENTAL_CHOICE)
                .first(ReservationState.RENTAL_BOOKING_REQUEST, guards.doRentalBooking())
                .last(ReservationState.IN_PROGRESS)
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_REQUEST).target(ReservationState.RENTAL_BOOKING_CONFIRMED)
                    .event(ReservationEvent.RENTAL_BOOKING_CONFIRMED_EVENT)
                    .action(actions.rentalReservationConfirmedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_REQUEST).target(ReservationState.RENTAL_BOOKING_DECLINED)
                    .event(ReservationEvent.RENTAL_BOOKING_DECLINED_EVENT)
                    .action(actions.rentalReservationDeclinedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_CONFIRMED).target(ReservationState.IN_PROGRESS)
                    .event(ReservationEvent.RENTAL_BOOKING_COMPLETED_EVENT)
                    .action(actions.rentalReservationCompletedAction(), errorHandler.handleError())
                    .and()

            // Flight
            .withChoice()
                .source(ReservationState.FLIGHT_CHOICE)
                .first(ReservationState.FLIGHT_BOOKING_REQUEST, guards.doFlightBooking())
                .last(ReservationState.IN_PROGRESS)
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_REQUEST).target(ReservationState.FLIGHT_BOOKING_CONFIRMED)
                    .event(ReservationEvent.FLIGHT_BOOKING_CONFIRMED_EVENT)
                    .action(actions.flightReservationConfirmedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_REQUEST).target(ReservationState.FLIGHT_BOOKING_DECLINED)
                    .event(ReservationEvent.FLIGHT_BOOKING_DECLINED_EVENT)
                    .action(actions.flightReservationDeclinedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_CONFIRMED).target(ReservationState.IN_PROGRESS)
                    .event(ReservationEvent.FLIGHT_BOOKING_COMPLETED_EVENT)
                    .action(actions.flightReservationCompletedAction(), errorHandler.handleError())
                    .and()

            //  Payment
            .withExternal()
                    .source(ReservationState.PAYMENT_REQUEST_INIT).target(ReservationState.PAYMENT_APPROVED)
                    .event(ReservationEvent.PAYMENT_CONFIRMED_EVENT)
                    .action(actions.paymentConfirmedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.PAYMENT_REQUEST_INIT).target(ReservationState.PAYMENT_DECLINED)
                    .event(ReservationEvent.PAYMENT_DECLINED_EVENT)
                    .action(actions.paymentDeclinedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.PAYMENT_APPROVED).target(ReservationState.SEND_INVOICE)
                    .event(ReservationEvent.SEND_INVOICE_EVENT)
                    .action(actions.sendInvoiceAction(), errorHandler.handleError())
                .and()

                // Send Invoice & Trip Plans
                .withExternal()
                    .source(ReservationState.SEND_INVOICE).target(ReservationState.SEND_TRAVEL_DETAILS)
                    .event(ReservationEvent.SEND_TRAVEL_DETAILS_EVENT)
                    .action(actions.sendTravelDetailsAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.SEND_TRAVEL_DETAILS).target(ReservationState.TRIP_CONFIRMED)
                    .event(ReservationEvent.TRIP_CONFIRMED_EVENT)
                    .action(actions.tripConfirmedAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.PAYMENT_DECLINED).target(ReservationState.TRIP_CANCELLED)
                    .event(ReservationEvent.TRIP_CANCELLED_EVENT)
                    .action(actions.tripCancelledAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.TRIP_CONFIRMED).target(ReservationState.RESERVATION_COMPLETED)
                    .event(ReservationEvent.AUTO_TRANSITION_EVENT)
                    .action(actions.reservationCompleted(), errorHandler.handleError())

                // Rollback - Compensating Transaction
                .and()
                .withExternal()
                    .source(ReservationState.TRIP_CANCELLED).target(ReservationState.ROLLBACK_IN_PROGRESS)
                    .event(ReservationEvent.START_ROLLBACK_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.ROLLBACK_IN_PROGRESS).target(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                    .event(ReservationEvent.FLIGHT_ROLLBACK_EVENT)
                    .action(actions.flightReservationRollbackAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.ROLLBACK_IN_PROGRESS).target(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                    .event(ReservationEvent.RENTAL_ROLLBACK_EVENT)
                    .action(actions.rentalReservationRollbackAction(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.ROLLBACK_IN_PROGRESS).target(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                    .event(ReservationEvent.HOTEL_ROLLBACK_EVENT)
                    .action(actions.hotelReservationRollbackAction(), errorHandler.handleError())
                .and()

                // Rollback Acknowledgement from the Services
                .withExternal()
                    .source(ReservationState.ROLLBACK_ACK_IN_PROGRESS).target(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                    .event(ReservationEvent.FLIGHT_ROLLBACK_ACK_EVENT)
                    .action(actions.flightReservationRollbackAck(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.ROLLBACK_ACK_IN_PROGRESS).target(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                    .event(ReservationEvent.RENTAL_ROLLBACK_ACK_EVENT)
                    .action(actions.rentalReservationRollbackAck(), errorHandler.handleError())
                .and()
                .withExternal()
                    .source(ReservationState.ROLLBACK_ACK_IN_PROGRESS).target(ReservationState.ROLLBACK_ACK_IN_PROGRESS)
                    .event(ReservationEvent.HOTEL_ROLLBACK_ACK_EVENT)
                    .action(actions.hotelReservationRollbackAck(), errorHandler.handleError())
                .and()

                // Final Rollback
                .withExternal()
                    .source(ReservationState.HOTEL_BOOKING_CANCELLED).target(ReservationState.ROLLBACK)
                    .event(ReservationEvent.ROLLBACK_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.RENTAL_BOOKING_CANCELLED).target(ReservationState.ROLLBACK)
                    .event(ReservationEvent.ROLLBACK_EVENT)
                .and()
                .withExternal()
                    .source(ReservationState.FLIGHT_BOOKING_CANCELLED).target(ReservationState.ROLLBACK)
                    .event(ReservationEvent.ROLLBACK_EVENT)
                .and()

                // Reservation Terminated After Receiving All the Rollback Acks
                .withExternal()
                    .source(ReservationState.ROLLBACK).target(ReservationState.RESERVATION_TERMINATED)
                    .event(ReservationEvent.ROLLBACK_COMPLETE_EVENT)
                    .guard(guards.isAllAcksReceived())
                    .action(actions.reservationTerminated(), errorHandler.handleError())

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
