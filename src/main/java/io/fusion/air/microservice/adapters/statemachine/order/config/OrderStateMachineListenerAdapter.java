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
package io.fusion.air.microservice.adapters.statemachine.order.config;

import io.fusion.air.microservice.domain.statemachine.order.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class OrderStateMachineListenerAdapter extends StateMachineListenerAdapter<OrderState, OrderEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private OrderHistoryService orderHistoryService;

    /**
     * Log when the State changes.
     * @param from
     * @param to
     */
    @Override
    public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
        if(from != null && to != null) {
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println("(5) LISTENER STATE TRANSITION ================================================================= >>");
            System.out.println("--------------------------------------------------------------------------------------------------");
            logStateTransition(from, to);
            orderHistoryService.saveOrderHistory(from.getId(), to.getId());
            System.out.println("--------------------------------------------------------------------------------------------------");
        }
    }

    @Override
    public void eventNotAccepted(Message<OrderEvent> event) {
        // System.out.println("--------------------------------------------------------------------------------------------------");
        // System.out.println("(3) LISTENER EVENT NOT ACCEPTED  ====== <"+event+"> =============================== >>");
        // System.out.println("--------------------------------------------------------------------------------------------------");
    }

    @Override
    public void transition(Transition<OrderState, OrderEvent> transition) {
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
     * Handle the Errors thrown by the Actions / Guards
     * @param stateMachine
     * @param exception
     */
    @Override
    public void stateMachineError(StateMachine<OrderState, OrderEvent> stateMachine, Exception exception) {
        System.out.println("STATE ERROR 1: ================================================== >>");

        // Capture the current state before transitioning to the error state
        OrderState errorSourceState = stateMachine.getState().getId();
        stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_SOURCE, errorSourceState);
        stateMachine.getExtendedState().getVariables().put(OrderConstants.ERROR_MSG, exception.getMessage());
        // Log the exception and the source state
        log.error("<><><> Exception occurred during state transition from state: " + errorSourceState, exception);

        // Handle the Error and Send a Failure Event to State Machine
        // stateMachine.sendEvent(OrderEvent.FAILURE_EVENT);
    }

    private void logStateTransition(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
        OrderState source = (from != null) ? from.getId() : null;
        OrderState target = (to != null) ? to.getId() : null;
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        log.info("LISTENER: STATE TRANSITIONING FROM [{}] TO ({})", s, t);
    }

}
