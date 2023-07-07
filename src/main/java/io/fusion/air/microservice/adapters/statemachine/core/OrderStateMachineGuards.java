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

import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import io.fusion.air.microservice.domain.statemachine.OrderConstants;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;
import io.fusion.air.microservice.utils.Utils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Order State Machine Guards
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class OrderStateMachineGuards {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Bean
    public Guard<OrderState, OrderEvent> creditCheckRequiredGuard() {
        return context -> {
            // Following Method is Required if the OrderEntity is Send as a Message (sendMessage)
            // As the OrderEntity is Set as an Extended State following call is NOT Required.
            // OrderEntity order = (OrderEntity) context.getMessageHeader(OrderConstants.ORDER_HEADER);

            // Extract OrderEntity from the Extended State
            OrderEntity order = context.getExtendedState().get(OrderConstants.ORDER_HEADER, OrderEntity.class);
            if(order != null) {
                log.info("Order = " + Utils.toJsonString(order));
                // Returns TRUE if the Order Value is greater than Rs. 1,00,000/-
                return order.getTotalOrderValue().compareTo(new BigDecimal("100000")) > 0;
            }
            // No CREDIT Check Required
            return false;
        };
    }
}
