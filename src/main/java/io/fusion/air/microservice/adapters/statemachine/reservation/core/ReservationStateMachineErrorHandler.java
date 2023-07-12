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
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationNotes;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
// Spring State Machine
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
// Java
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

/**
 * Reservation State Machine Error Handler
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Component
public class ReservationStateMachineErrorHandler {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Bean(name = "Reservation-Error-Handler")
    public Action<ReservationState, ReservationEvent> handleError() {
        return context -> {
            System.out.println("STATE ERROR 2: ================================================== >>");
            // Order Notes Object to capture errors
            ReservationNotes error = createReservationNotes(context);
            // Get the State Machine from the context
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Store the Order Notes in Extended State
                stateMachine.getExtendedState().getVariables().put(ReservationConstants.ERROR_OBJECT, error);
                // Extract Order from the Extended State
                ReservationEntity order = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                if(order != null) {
                    // Create Message for the Failure Event
                    Message mesg = MessageBuilder.withPayload(ReservationEvent.FAILURE_EVENT)
                            .setHeader(ReservationConstants.RESERVATION_ID_HEADER, order.getReservationId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                }
            }
        };
    }

    /**
     * Create Reservation Notes from the Context
     * @param context
     */
    private ReservationNotes createReservationNotes(StateContext<ReservationState, ReservationEvent> context) {
        // Extract Reservation States and Events
        ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
        ReservationState source = context.getSource().getId();
        ReservationState target = context.getTarget().getId();
        ReservationEvent event = context.getEvent();
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        String e = (event != null) ?  event.name() : "No-Event";
        String o = (reservation != null) ? reservation.getReservationId() : "No-Reservation-Found!";
        String errorMessage = (context.getException() != null) ? context.getException().getMessage() : "";
        log.info("TRANSITIONING FAILED: FROM [{}] TO ({}) based on EVENT = <{}>", s, t,e);
        log.info("{} for Order ID = {}",errorMessage,o);
        return new ReservationNotes(s,t,e, "", errorMessage);
    }
}
