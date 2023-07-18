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
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationConstants;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
// Spring State Machine
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
// Java
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation State Machine Rollback Actions
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Component
public class ReservationStateMachineRollbackActions {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    // ROLL BACK Actions
    /**
     * Checks if the Flight Rollback required or Not
     * @return
     */
    @Bean(name = "Reservation-Flight-Rollback-Required")
    public Action<ReservationState, ReservationEvent> checkFlightRollbackRequired() {
        return context -> {
            logStateTransition(context, "Check Flight Rollback Required or Not");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    /**
     * Flight Booking Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Flight-Rollback-Event")
    public Action<ReservationState, ReservationEvent> flightRollBackAction() {
        return context -> {
            sendEvent(context, ReservationEvent.FLIGHT_ROLLBACK_EVENT);
        };
    }

    /**
     * Checks if the Rental Rollback required or Not
     * @return
     */
    @Bean(name = "Reservation-Flight-Rollback-Required")
    public Action<ReservationState, ReservationEvent> checkRentalRollbackRequired() {
        return context -> {
            logStateTransition(context, "Check Rental Rollback Required or Not");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    /**
     * Rental Booking Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Rental-Rollback-Event")
    public Action<ReservationState, ReservationEvent> rentalRollBackAction() {
        return context -> {
            sendEvent(context, ReservationEvent.RENTAL_ROLLBACK_EVENT);
        };
    }

    /**
     * Checks if the Hotel Rollback required or Not
     * @return
     */
    @Bean(name = "Reservation-Flight-Rollback-Required")
    public Action<ReservationState, ReservationEvent> checkHotelRollbackRequired() {
        return context -> {
            logStateTransition(context, "Check Hotel Rollback Required or Not");
            // Add Business Logic to Handle Event
            // ...
        };
    }

    /**
     * Hotel Booking Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Hotel-Rollback-Event")
    public Action<ReservationState, ReservationEvent> hotelRollBackAction() {
        return context -> {
            sendEvent(context, ReservationEvent.HOTEL_ROLLBACK_EVENT);
        };
    }

    @Bean(name = "Reservation-Start-Rollback-Event")
    public Action<ReservationState, ReservationEvent> startRollbackAction() {
        return context -> {
            sendEvent(context, ReservationEvent.START_ROLLBACK_EVENT);
        };
    }

    @Bean(name = "Reservation-Start-Rollback-Process")
    public Action<ReservationState, ReservationEvent> startRollbackProcess() {
        return context -> {
            System.out.println("(6.x) [-] TRANSITIONING: == (StateMachineRollbackActions) ============================== >>");
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Reservation from the Extended State
                ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                int x = 1;
                for(ReservationEvent event : getRollbackEvents() ){
                    if(checkIfRollbackRequired(reservation, event)) {
                        System.out.println("(6."+x+") [-] TRANSITIONING: == (StateMachineRollbackActions) ============================== >> "+event.name());
                        sendMessage(event, reservation.getReservationId(), stateMachine, context);
                        x++;
                    }
                }
                System.out.println("(6."+x+") [-] TRANSITIONING: == (StateMachineRollbackActions) ============================== >> "+ReservationEvent.ROLLBACK_EVENT.name());
                // Call Final Rollback
                rollBackAction();
            } else {
                logStateTransition(context, "ERROR GETTING STATE MACHINE FROM THE CONTEXT!!!!!");
            }
        };
    }

    /**
     * Final Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Rollback-Event")
    public Action<ReservationState, ReservationEvent> rollBackAction() {
        return context -> {
            sendEvent(context, ReservationEvent.ROLLBACK_EVENT);
        };
    }

    /**
     * Send Event for the State Context
     * @param context
     * @param event
     */
    private void sendEvent(StateContext<ReservationState, ReservationEvent> context, ReservationEvent event) {
        System.out.println("(6.x) {"+event.name()+"} TRANSITIONING: == (StateMachineRollbackActions) ============================== >> "+event.name());
        StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
        if(stateMachine != null) {
            // Extract Reservation from the Extended State
            ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
            if(reservation != null) {
                sendMessage(event, reservation.getReservationId(), stateMachine, context);
            }
        } else {
            logStateTransition(context, "ERROR GETTING STATE MACHINE FROM THE CONTEXT!!!!!");
        }
    }

    private void sendMessage(ReservationEvent event, String reservationId,
                             StateMachine<ReservationState, ReservationEvent> stateMachine,
                             StateContext<ReservationState, ReservationEvent> context) {
        if(event == null || stateMachine == null || context == null) {
            log.info("Invalid Input to sendMessage() method!");
            return;
        }
        // Create Message for the Auto Transition Event
        Message mesg = MessageBuilder.withPayload(event)
                .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservationId)
                .build();
        // Send Event to the State Machine
        stateMachine.sendEvent(mesg);
        logStateTransition(context, event.name()+" SEND!");
    }

    /**
     * Returns TRUE if the Rollback is required for a specific event
     * @param reservation
     * @param event
     * @return
     */
    private boolean checkIfRollbackRequired(ReservationEntity reservation, ReservationEvent event) {
        if(reservation == null || event == null) {
            log.info("Invalid Input to checkIfRollbackRequired() method!");
            return false;
        }
        switch(event) {
            case HOTEL_ROLLBACK_EVENT:
                return reservation.isHotelBookingAvailable();
            case RENTAL_ROLLBACK_EVENT:
                return reservation.isRentalBookingAvailable();
            case FLIGHT_ROLLBACK_EVENT:
                return reservation.isFlightBookingAvailable();
        }
        return false;
    }

    private ReservationEvent[] getRollbackEvents() {
        ReservationEvent[] events = new ReservationEvent[3];
        events[0] = ReservationEvent.FLIGHT_ROLLBACK_EVENT;
        events[1] = ReservationEvent.RENTAL_ROLLBACK_EVENT;
        events[2] = ReservationEvent.HOTEL_ROLLBACK_EVENT;
        return events;
    }

    /**
     * Log the State Transition Details
     * @param context
     * @param _msg
     */
    private void logStateTransition(StateContext<ReservationState, ReservationEvent> context, String _msg) {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("(7) STATE ACTION on EVENT  == (StateMachineActions) =========================================== >>");
        System.out.println("--------------------------------------------------------------------------------------------------");

        ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
        ReservationState source = context.getSource().getId();
        ReservationState target = context.getTarget().getId();
        ReservationEvent event = context.getEvent();
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        String e = (event != null) ?  event.name() : "No-Event";
        String o = (reservation != null) ? reservation.getReservationId() : "No-Reservation-Found!";
        log.info("TRANSITIONING FROM [{}] TO ({}) based on EVENT = <{}>", s, t,e);
        log.info("{} for Reservation ID = {}",_msg,o);
    }
}
