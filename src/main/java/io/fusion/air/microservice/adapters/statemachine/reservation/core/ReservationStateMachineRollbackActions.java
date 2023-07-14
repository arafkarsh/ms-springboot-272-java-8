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
import io.fusion.air.microservice.domain.exceptions.BusinessServiceException;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationConstants;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation State Machine Actions
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
     * Flight Booking Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Flight-Rollback-Event")
    public Action<ReservationState, ReservationEvent> flightRollBackAction() {
        return context -> {
            System.out.println("(6.1) FLIGHT_ROLLBACK TRANSITIONING: == (StateMachineActions) ==================================== >>");
            // Get the State Machine from the context
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Reservation from the Extended State
                ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                if(reservation != null) {
                    // Create Message for the Auto Transition Event
                    Message mesg = MessageBuilder.withPayload(ReservationEvent.FLIGHT_ROLLBACK_EVENT)
                            .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                    logStateTransition(context, "FLIGHT_ROLLBACK_EVENT SEND: ");
                }
            } else {
                logStateTransition(context, "ERROR GETTING STATE MACHINE FROM THE CONTEXT!!!!!");
            }
        };
    }

    /**
     * Rental Booking Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Rental-Rollback-Event")
    public Action<ReservationState, ReservationEvent> rentalRollBackAction() {
        return context -> {
            System.out.println("(6.2) RENTAL_ROLLBACK TRANSITIONING: == (StateMachineActions) ==================================== >>");
            // Get the State Machine from the context
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Reservation from the Extended State
                ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                if(reservation != null) {
                    // Create Message for the Auto Transition Event
                    Message mesg = MessageBuilder.withPayload(ReservationEvent.RENTAL_ROLLBACK_EVENT)
                            .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                    logStateTransition(context, "RENTAL_ROLLBACK_EVENT SEND: ");
                }
            } else {
                logStateTransition(context, "ERROR GETTING STATE MACHINE FROM THE CONTEXT!!!!!");
            }
        };
    }

    /**
     * Hotel Booking Rollback Action
     * @return
     */
    @Bean(name = "Reservation-Hotel-Rollback-Event")
    public Action<ReservationState, ReservationEvent> hotelRollBackAction() {
        return context -> {
            System.out.println("(6.3) HOTEL_ROLLBACK TRANSITIONING: == (StateMachineActions) ==================================== >>");
            // Get the State Machine from the context
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Reservation from the Extended State
                ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                if(reservation != null) {
                    // Create Message for the Auto Transition Event
                    Message mesg = MessageBuilder.withPayload(ReservationEvent.HOTEL_ROLLBACK_EVENT)
                            .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                    logStateTransition(context, "HOTEL_ROLLBACK_EVENT SEND: ");
                }
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
            System.out.println("(6.3) ROLLBACK_EVENT TRANSITIONING: == (StateMachineActions) ==================================== >>");
            // Get the State Machine from the context
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Reservation from the Extended State
                ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                if(reservation != null) {
                    // Create Message for the Auto Transition Event
                    Message mesg = MessageBuilder.withPayload(ReservationEvent.ROLLBACK_EVENT)
                            .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                    logStateTransition(context, "ROLLBACK_EVENT SEND: ");
                }
            } else {
                logStateTransition(context, "ERROR GETTING STATE MACHINE FROM THE CONTEXT!!!!!");
            }
        };
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
