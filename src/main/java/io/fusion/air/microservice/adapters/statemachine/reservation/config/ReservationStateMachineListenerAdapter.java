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
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationConstants;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
// Spring State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
// Java
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class ReservationStateMachineListenerAdapter extends StateMachineListenerAdapter<ReservationState, ReservationEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    // @Autowired
    // private ReservationHistoryService reservationHistory;

    /**
     * Log when the State changes.
     * @param from
     * @param to
     */
    @Override
    public void stateChanged(State<ReservationState, ReservationEvent> from, State<ReservationState, ReservationEvent> to) {
        if(from != null && to != null) {
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println("(5) LISTENER STATE TRANSITION ================================================================= >>");
            System.out.println("--------------------------------------------------------------------------------------------------");
            logStateTransition(from, to);
            // reservationHistory.saveReservationHistory(from.getId(), to.getId());
            System.out.println("--------------------------------------------------------------------------------------------------");
        }
    }

    @Override
    public void eventNotAccepted(Message<ReservationEvent> event) {
        // System.out.println("--------------------------------------------------------------------------------------------------");
        // System.out.println("(3) LISTENER EVENT NOT ACCEPTED  ====== <"+event+"> =============================== >>");
        // System.out.println("--------------------------------------------------------------------------------------------------");
    }

    @Override
    public void transition(Transition<ReservationState, ReservationEvent> transition) {
        /**
        String src = (transition.getSource() != null) ? transition.getSource().getId().name() : "NoSource";
        String dst = (transition.getTarget() != null) ? transition.getTarget().getId().name() : "NoTarget";
        if(transition.getSource() != null) {
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println("(2) LISTENER TRANSITION == FROM [" + src + "]  TO [" + dst + "] ========= >>");
            System.out.println("--------------------------------------------------------------------------------------------------");
            //  log.info("Transition from {} to {}", transition.getSource().getId(), transition.getTarget().getId());
        }
         */
    }

    /**
     * Handle the Errors throw by the Actions / Guards
     * @param stateMachine
     * @param exception
     */
    @Override
    public void stateMachineError(StateMachine<ReservationState, ReservationEvent> stateMachine, Exception exception) {
        System.out.println("STATE ERROR 1: ================================================== >>");

        // Capture the current state before transitioning to the error state
        ReservationState errorSourceState = stateMachine.getState().getId();
        stateMachine.getExtendedState().getVariables().put(ReservationConstants.ERROR_SOURCE, errorSourceState);
        stateMachine.getExtendedState().getVariables().put(ReservationConstants.ERROR_MSG, exception.getMessage());
        // Log the exception and the source state
        log.error("<><><> Exception occurred during state transition from state: " + errorSourceState, exception);

        // Handle the Error and Send a Failure Event to State Machine
        // stateMachine.sendEvent(OrderEvent.FAILURE_EVENT);
    }

    private void logStateTransition(State<ReservationState, ReservationEvent> from, State<ReservationState, ReservationEvent> to) {
        ReservationState source = (from != null) ? from.getId() : null;
        ReservationState target = (to != null) ? to.getId() : null;
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        log.info("LISTENER: STATE TRANSITIONING FROM [{}] TO ({})", s, t);
    }

}
