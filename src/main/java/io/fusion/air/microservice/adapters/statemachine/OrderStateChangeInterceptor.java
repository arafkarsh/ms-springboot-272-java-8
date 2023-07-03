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
package io.fusion.air.microservice.adapters.statemachine;

import io.fusion.air.microservice.adapters.repository.OrderRepository;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

/**
 * Order State Change Listener
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderState, OrderEvent> {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     * @param rootStateMachine
     */
    @Override
    public void preStateChange(State<OrderState, OrderEvent> state,
                               Message<OrderEvent> message, Transition<OrderState, OrderEvent> transition,
                               StateMachine<OrderState, OrderEvent> stateMachine,
                               StateMachine<OrderState, OrderEvent> rootStateMachine) {


    }
}
