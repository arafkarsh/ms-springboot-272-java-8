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
package io.fusion.air.microservice.adapters.statemachine.reservation.core;
// Custom
import io.fusion.air.microservice.adapters.statemachine.reservation.config.ReservationStateChangeInterceptor;
import io.fusion.air.microservice.adapters.statemachine.reservation.config.ReservationStateDetails;
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.ports.services.ReservationStateMachineService;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationConstants;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
// Java
import java.util.List;
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation Service
 * Reservation Processing is implemented with Spring State Machine
 *
 * 1. Request for Hotel Booking
 * 2. Request for Rental Booking
 * 3. Request for Flight Booking
 * 4. Request for Payment
 * 5. Request to Send Invoices and Travel Plans
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
@RequestScope
public class ReservationStateMachineManager implements ReservationStateMachineService {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private StateMachineFactory<ReservationState, ReservationEvent> stateMachineFactory;

    @Autowired
    private ReservationStateChangeInterceptor reservationStateChangeInterceptor;

    /**
     * Check for Valid Reservation
     *
     * @param reservation
     * @return
     */
    @Override
    public void reservationValidation(ReservationEntity reservation) {
        sendEvent(ReservationEvent.RESERVATION_VALIDATION_EVENT, reservation);
    }

    /**
     * Initialize Reservation
     *
     * @param reservation
     */
    @Override
    public void reservationInit(ReservationEntity reservation) {
        sendEvent(ReservationEvent.RESERVATION_INIT_EVENT, reservation);
    }

    /**
     * Hotel booking request
     *
     * @param reservation
     */
    @Override
    public void hotelBookingRequest(ReservationEntity reservation) {
        sendEvent(ReservationEvent.HOTEL_BOOKING_REQUEST_EVENT, reservation);
    }

    /**
     * Hotel Booking Confirmed
     *
     * @param reservation
     */
    @Override
    public void hotelBookingConfirmed(ReservationEntity reservation) {
        sendEvent(ReservationEvent.HOTEL_BOOKING_CONFIRMED_EVENT, reservation);
    }

    /**
     * Hotel Booking declined
     *
     * @param reservation
     */
    @Override
    public void hotelBookingDeclined(ReservationEntity reservation) {
        sendEvent(ReservationEvent.HOTEL_BOOKING_DECLINED_EVENT, reservation);
    }

    /**
     * Hotel Booking Rollback
     *
     * @param reservation
     */
    @Override
    public void hotelBookingRollback(ReservationEntity reservation) {
        sendEvent(ReservationEvent.HOTEL_ROLLBACK_EVENT, reservation);
    }

    /**
     * Rental booking request
     *
     * @param reservation
     */
    @Override
    public void rentalBookingRequest(ReservationEntity reservation) {
        sendEvent(ReservationEvent.RENTAL_BOOKING_REQUEST_EVENT, reservation);
    }

    /**
     * Rental Booking Confirmed
     *
     * @param reservation
     */
    @Override
    public void rentalBookingConfirmed(ReservationEntity reservation) {
        sendEvent(ReservationEvent.RENTAL_BOOKING_CONFIRMED_EVENT, reservation);
    }

    /**
     * Rental Booking declined
     *
     * @param reservation
     */
    @Override
    public void rentalBookingDeclined(ReservationEntity reservation) {
        sendEvent(ReservationEvent.RENTAL_BOOKING_DECLINED_EVENT, reservation);
    }

    /**
     * Rental Booking Rollback
     *
     * @param reservation
     */
    @Override
    public void rentalBookingRollback(ReservationEntity reservation) {
        sendEvent(ReservationEvent.RENTAL_ROLLBACK_EVENT, reservation);
    }

    /**
     * Flight booking request
     *
     * @param reservation
     */
    @Override
    public void flightBookingRequest(ReservationEntity reservation) {
        sendEvent(ReservationEvent.FLIGHT_BOOKING_REQUEST_EVENT, reservation);
    }

    /**
     * Flight Booking Confirmed
     *
     * @param reservation
     */
    @Override
    public void flightBookingConfirmed(ReservationEntity reservation) {
        sendEvent(ReservationEvent.FLIGHT_BOOKING_CONFIRMED_EVENT, reservation);
    }

    /**
     * Flight Booking declined
     *
     * @param reservation
     */
    @Override
    public void flightBookingDeclined(ReservationEntity reservation) {
        sendEvent(ReservationEvent.FLIGHT_BOOKING_DECLINED_EVENT, reservation);
    }

    /**
     * Flight Booking Rollback
     *
     * @param reservation
     */
    @Override
    public void flightBookingRollback(ReservationEntity reservation) {
        sendEvent(ReservationEvent.FLIGHT_ROLLBACK_EVENT, reservation);
    }

    /**
     * Payment request
     *
     * @param reservation
     */
    @Override
    public void paymentRequest(ReservationEntity reservation) {
        sendEvent(ReservationEvent.PAYMENT_REQUEST_EVENT, reservation);
    }

    /**
     * Payment Confirmed
     *
     * @param reservation
     */
    @Override
    public void paymentConfirmed(ReservationEntity reservation) {
        sendEvent(ReservationEvent.PAYMENT_CONFIRMED_EVENT, reservation);
    }

    /**
     * Payment declined
     *
     * @param reservation
     */
    @Override
    public void paymentDeclined(ReservationEntity reservation) {
        sendEvent(ReservationEvent.PAYMENT_DECLINED_EVENT, reservation);
    }

    /**
     * Invoice
     *
     * @param reservation
     */
    @Override
    public void sendInvoice(ReservationEntity reservation) {
        sendEvent(ReservationEvent.SEND_INVOICE_EVENT, reservation);
    }

    /**
     * Send Travel Plans
     *
     * @param reservation
     */
    @Override
    public void sendTravelPlans(ReservationEntity reservation) {
        sendEvent(ReservationEvent.SEND_TRAVEL_DETAILS_EVENT, reservation);
    }

    /**
     * Trip Confirmed
     *
     * @param reservation
     */
    @Override
    public void tripConfirmed(ReservationEntity reservation) {
        sendEvent(ReservationEvent.TRIP_CONFIRMED_EVENT, reservation);
    }

    /**
     * Trip Cancelled
     *
     * @param reservation
     */
    @Override
    public void tripCancelled(ReservationEntity reservation) {
        sendEvent(ReservationEvent.TRIP_CANCELLED_EVENT, reservation);
    }

    /**
     * Sends Multiple Events to the State Machine
     * @param reservation
     * @param events
     */
    public void multipleEvents(ReservationEntity reservation, List<ReservationEvent> events) {
        sendEvent(reservation, events);
    }

    // =======================================================================================================
    // Restore State Machine and Send Message / Event to State Machine
    // =======================================================================================================

    /**
     * Send Message/Event to State Machine
     * @param event
     * @param reservation
     */
    public void sendEvent(ReservationEvent event, ReservationEntity reservation) {
       validateInputs(event, reservation);
        // Restore the state Machine based on Reservation ID
        StateMachine<ReservationState, ReservationEvent> sm = restoreStateMachine(reservation);
        // Create Message with the reservation Event and Set the Reservation ID in the Header
        Message mesg = MessageBuilder.withPayload(event)
                .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                .build();
        // Send the Message to the State Machine
        log.info("SENDING MESSAGE >> "+event.name());
        sm.sendEvent(mesg);
    }

    /**
     * Send Multiple Events
     * @param reservation
     * @param events
     */
    private void sendEvent(ReservationEntity reservation, List<ReservationEvent> events ) {
        validateInputs(events, reservation);
        // Restore the state Machine based on Reservation ID
        StateMachine<ReservationState, ReservationEvent> sm = restoreStateMachine(reservation);
        // Send Multiple Events
        for(ReservationEvent event : events) {
            // Create Message with the Reservation Event and Set the Reservation ID in the Header
            Message mesg = MessageBuilder.withPayload(event)
                    .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                    .build();
            // Send the Message to the State Machine
            sm.sendEvent(mesg);
        }
    }

    /**
     * Return the State Machine with Current reservation State
     *
     * @param reservation
     * @return
     */
    private StateMachine<ReservationState, ReservationEvent> restoreStateMachine(ReservationEntity reservation) {
        if(reservation == null) {
            throw new InputDataException("Invalid Reservation (null) to Restore the State Machine!");
        }
        log.info("Restore State Machine For Reservation Id = "+reservation.getReservationId()+" ReservationState = "+reservation.getReservationState());
        StateMachine<ReservationState, ReservationEvent> sm = stateMachineFactory.getStateMachine(reservation.getReservationId());
        // Stop The State Machine
        sm.stop();
        // Set the State (Reservation) retrieved from the Database
        sm.getStateMachineAccessor()
            .doWithAllRegions(sma -> {
                // Add State Change Interceptor
                sma.addStateMachineInterceptor(reservationStateChangeInterceptor);
                // Set the Current Reservation State with State Machine
                sma.resetStateMachine(new DefaultStateMachineContext<>(reservation.getReservationState(),
                            null, null, null));
                }
            );
        // Set the Reservation in the Extended State of the State Machine
        sm.getExtendedState().getVariables().put(ReservationConstants.RESERVATION_HEADER, reservation);
        // Start the State Machine with new Reservation State (Set from DB)
        sm.start();
        // Return the State  Machine
        return sm;
    }

    /**
     * Validate Inputs
     * @param event
     * @param reservation
     * @return
     */
    private boolean validateInputs(ReservationEvent event, ReservationEntity reservation) {
        if(event == null) {
            throw new InputDataException("Invalid ReservationEvent! to Send Event");
        }
        if(reservation == null) {
            throw new InputDataException("Invalid Reservation! to Send Event");
        }
        return true;
    }

    private boolean validateInputs(List<ReservationEvent> events, ReservationEntity reservation) {
        if(events == null) {
            throw new InputDataException("Invalid ReservationEvent! to Send Event");
        }
        if(reservation == null) {
            throw new InputDataException("Invalid Reservation! to Send Event");
        }
        return true;
    }
}
