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

import io.fusion.air.microservice.adapters.repository.OrderRepository;
import io.fusion.air.microservice.domain.entities.order.OrderEntity;
import io.fusion.air.microservice.domain.entities.order.OrderStateHistoryEntity;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderNotes;
import io.fusion.air.microservice.domain.statemachine.order.OrderResult;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
import io.fusion.air.microservice.utils.Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
@RequestScope
public class OrderHistoryService {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStateDetails orderStateDetails;

    @Transactional
    public void saveOrderHistory(OrderStateDetails osd) {
        OrderState source = osd.getValue(OrderStateDetails.SOURCE);
        OrderState target = osd.getValue(OrderStateDetails.TARGET);
        OrderEvent event =  osd.getEvent();
        OrderEntity order = osd.getOrder();
        OrderNotes notes = osd.getNotes();
        saveOrderHistory(source, target, event, order, notes);
    }

    @Transactional
    public void saveOrderHistory(OrderState source, OrderState target) {
        if(orderStateDetails.getOrder().getOrderState().getRank() < target.getRank()) {
            orderStateDetails.addProperties(OrderStateDetails.SOURCE, source);
            orderStateDetails.addProperties(OrderStateDetails.TARGET, target);
            OrderEvent event = orderStateDetails.getEvent();
            OrderEntity order = orderStateDetails.getOrder();
            OrderNotes notes = orderStateDetails.getNotes();
            saveOrderHistory(source, target, event, order, notes);
        } else {
            log.info("STATE ALREADY SAVED!");
        }
    }

    @Transactional
    public void saveOrderHistory(OrderState source, OrderState target, OrderEvent event,
                                 OrderEntity order, OrderNotes errorObj) {
        if(!validateInputs(source, target, event, order)) {
            return;
        }
        if(source.name().equalsIgnoreCase(target.name())) {
            log.info("NOT SAVING: Source {} and Target {} same!", source.name(), target.name());
            return;
        }
        OrderResult result = null;
        String notes = "";
        // If the Event is a FAILURE Event thrown by Exceptions
        if(event != null && event.equals(OrderEvent.FAILURE_EVENT)) {
            notes = Utils.toJsonString(errorObj);
            result = OrderResult.fromString(errorObj.getTargetState());
        }
        // Check if there are any Results Available based on Target State
        if(result == null) {
            result = OrderResult.fromString(target.name());
        }
        OrderStateHistoryEntity history = new OrderStateHistoryEntity(source, target, event, order.getVersion() + 1, notes);
        order.addOrderStateHistory(history);
        order.setState(target);
        // If Results Available then Set the Result in Order Object
        if(result != null) {
            order.setOrderResult(result);
        }
        // Save the Order with the History Details
        if(target.getRank() >= order.getOrderState().getRank()) {
            orderRepository.save(order);
            orderStateDetails.stateSaved();
        } else {
            log.info("ORDER HISTORY NOT SAVED TARGET RANK IS {} < {} ORDER STATE RANK! ", target.getRank(),order.getOrderState().getRank());
        }
    }

    private boolean validateInputs(OrderState source, OrderState target, OrderEvent event, OrderEntity order) {
        boolean status = true;
        if(source == null) {
            log.error("Invalid (NULL) source!");
            status = false;
        }
        if(target == null) {
            log.error("Invalid (NULL) target!");
            status = false;
        }
        if(event == null) {
            log.error("Invalid (NULL) event!");
            status = false;
        }
        if(order == null) {
            log.error("Invalid (NULL) order!");
            status = false;
        }
        return status;
    }
}
