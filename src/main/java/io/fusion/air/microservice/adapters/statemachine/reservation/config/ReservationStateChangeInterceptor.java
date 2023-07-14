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
import io.fusion.air.microservice.adapters.repository.ReservationRepository;
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.entities.reservation.ReservationStateHistoryEntity;
import io.fusion.air.microservice.domain.statemachine.reservation.*;
import io.fusion.air.microservice.utils.Utils;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
// Java
import java.util.Optional;
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation State Change Listener
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class ReservationStateChangeInterceptor extends StateMachineInterceptorAdapter<ReservationState, ReservationEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationHistoryService reservationHistoryService;

    /**
     * Based on the State change by the Reservation Event
     * Set the new state in the Reservation Object and Save the State.
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     * @param rootStateMachine
     */
    @Override
    @Transactional
    public void postStateChange(State<ReservationState, ReservationEvent> state,
                               Message<ReservationEvent> message,
                               Transition<ReservationState, ReservationEvent> transition,
                               StateMachine<ReservationState, ReservationEvent> stateMachine,
                               StateMachine<ReservationState, ReservationEvent> rootStateMachine) {

        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(String.class.cast(msg.getHeaders().getOrDefault(ReservationConstants.RESERVATION_ID_HEADER, "")))
                .ifPresent(orderId -> {
                    // Optional<ReservationEntity> orderOpt = reservationRepository.findByReservationId(Utils.getUUID(orderId));
                    ReservationEntity reservation = stateMachine.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                    String errorSource = "", errorMsg = "", notes = "";
                    if(reservation != null) {
                        // ReservationEntity order = orderOpt.get();
                        ReservationState source = reservation.getReservationState();
                        ReservationState target = state.getId();
                        ReservationEvent event  = null;
                        if(transition.getTrigger() != null) {
                            event = transition.getTrigger().getEvent();
                            source = transition.getSource().getId();
                        }
                        ReservationNotes errorObj = null;
                        try {
                            // If the Event is a FAILURE Event thrown by Exceptions
                            if(event != null && event.equals(ReservationEvent.FAILURE_EVENT)) {
                                errorObj = stateMachine.getExtendedState().get(ReservationConstants.ERROR_OBJECT, ReservationNotes.class);
                            }
                            reservationHistoryService.saveReservationHistory(source, target, event, reservation, errorObj);
                        } catch (Exception e) {
                            log.error("ERROR in OrderStateChangeListener! "+e.getMessage(),e);
                            e.printStackTrace();
                        } finally {
                            // Log the State Change
                            logStateChange(source, target, event);
                        }
                    } else {
                        log.info("Reservation Object is Missing in Extended State of the State Machine!");
                    }
                });
        });
    }

    /**
     * Log State Change
     * @param source
     * @param target
     * @param event
     */
    private void logStateChange(ReservationState source, ReservationState target, ReservationEvent event) {
        String s = (source != null) ? source.name() : "NO-SOURCE";
        String t = (target != null) ? target.name() : "NO-TARGET";
        String e = (event != null) ? event.name() : "NO-EVENT";
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("(4) STATE TRANSITION == (StateChangeInterceptor) =============================================== >> "+event);
        System.out.println("--------------------------------------------------------------------------------------------------");
        log.info("CHANGE RESERVATION STATE FROM >> [{}] TO ({})  Based on Event <{}>",s, t, e);
        System.out.println("--------------------------------------------------------------------------------------------------");

    }
}
