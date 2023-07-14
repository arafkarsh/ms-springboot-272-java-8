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
// Custom
import io.fusion.air.microservice.adapters.repository.OrderRepository;
import io.fusion.air.microservice.domain.entities.order.OrderEntity;
import io.fusion.air.microservice.domain.entities.order.OrderStateHistoryEntity;
import io.fusion.air.microservice.domain.statemachine.order.*;
import io.fusion.air.microservice.utils.Utils;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Sprong State Machine
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
// Java & SLF4J
import java.util.Optional;
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order State Change Listener
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderState, OrderEvent> {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderHistoryService orderHistoryService;

    /**
     * Based on the State change by the Order Event
     * Set the new state in the Order Object and Save the State.
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     * @param rootStateMachine
     */
    @Override
    @Transactional
    public void postStateChange(State<OrderState, OrderEvent> state,
                               Message<OrderEvent> message,
                               Transition<OrderState, OrderEvent> transition,
                               StateMachine<OrderState, OrderEvent> stateMachine,
                               StateMachine<OrderState, OrderEvent> rootStateMachine) {

        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(String.class.cast(msg.getHeaders().getOrDefault(OrderConstants.ORDER_ID_HEADER, "")))
                .ifPresent(orderId -> {
                    Optional<OrderEntity> orderOpt = orderRepository.findByOrderId(Utils.getUUID(orderId));
                    String errorSource = "", errorMsg = "", notes = "";
                    if(orderOpt.isPresent()) {
                        OrderEntity order = orderOpt.get();
                        OrderState source = order.getOrderState();
                        OrderState target = state.getId();
                        OrderEvent event  = null;
                        if(transition.getTrigger() != null) {
                            event = transition.getTrigger().getEvent();
                        }
                        OrderNotes errorObj = null;
                        try {
                            // If the Event is a FAILURE Event thrown by Exceptions
                            if(event != null && event.equals(OrderEvent.FAILURE_EVENT)) {
                                errorObj = stateMachine.getExtendedState().get(OrderConstants.ERROR_OBJECT, OrderNotes.class);
                            }
                            orderHistoryService.saveOrderHistory(source, target, event, order, errorObj);
                        } catch (Exception e) {
                            log.error("ERROR in OrderStateChangeListener! "+e.getMessage(),e);
                            e.printStackTrace();
                        } finally {
                            // Log the State Change
                            logStateChange(source, target, event);
                        }
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
    private void logStateChange(OrderState source, OrderState target, OrderEvent event) {
        String s = (source != null) ? source.name() : "NO-SOURCE";
        String t = (target != null) ? target.name() : "NO-TARGET";
        String e = (event != null) ? event.name() : "NO-EVENT";
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("(4) STATE TRANSITION == (StateChangeInterceptor) ============================================== >> "+event);
        System.out.println("--------------------------------------------------------------------------------------------------");
        log.info("CHANGE ORDER STATE FROM >> [{}] TO ({})  Based on Event <{}>",s, t, e);
        System.out.println("--------------------------------------------------------------------------------------------------");

    }
}
