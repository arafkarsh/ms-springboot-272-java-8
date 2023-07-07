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
package io.fusion.air.microservice.adapters.statemachine.core;

import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order State Machine Actions
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Service
public class OrderStateMachineActions {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Bean
    public Action<OrderState, OrderEvent> creditCheckAction() {
        return context -> {
            try {
                OrderState sourceState = context.getSource().getId();
                OrderState targetState = context.getTarget().getId();
                log.info("Transitioning from {} to {}", sourceState, targetState);
            } catch (Exception e) {

            }
        };
    }
}
