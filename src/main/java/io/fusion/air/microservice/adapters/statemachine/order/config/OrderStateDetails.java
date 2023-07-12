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

import io.fusion.air.microservice.domain.entities.order.OrderEntity;
import io.fusion.air.microservice.domain.statemachine.order.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.order.OrderNotes;
import io.fusion.air.microservice.domain.statemachine.order.OrderState;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Component
@RequestScope
public class OrderStateDetails implements Serializable {

    public static final String SOURCE = "SOURCE";
    public static final String TARGET = "TARGET";

    private OrderEntity order;
    private HashMap<String, OrderState> states = new HashMap<String, OrderState>();

    private OrderEvent event;

    private boolean stateSaved = false;

    private Exception exception;

    private OrderNotes notes;

    public OrderStateDetails() {}

    /**
     * Add Order
     * @param _order
     */
    public void addOrder(OrderEntity _order) {
        order = _order;
    }

    /**
     * Get Order
     * @return
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * Add Properties
     * @param key
     * @param value
     */
    public void addProperties(String key, OrderState value) {
        states.put(key, value);
    }

    /**
     * Return Properties
     * @param key
     * @return
     */
    public OrderState getValue(String key) {
        return states.get(key);
    }

    /**
     * Returns Event
     * @return
     */
    public OrderEvent getEvent() {
        return event;
    }

    /**
     * Set Event
     * @param event
     */
    public void setEvent(OrderEvent event) {
        this.event = event;
        this.stateSaved = false;
    }

    /**
     * Returns TRUE if the State is saved.
     * @return
     */
    public boolean isStateSaved() {
        return stateSaved;
    }

    /**
     * Set the State Saved = TRUE
     */
    public void stateSaved() {
        this.stateSaved = true;
    }

    /**
     * Get the Exception
     * @return
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Sets the Exception
     * @param exception
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Get Order Notes
     * @return
     */
    public OrderNotes getNotes() {
        return notes;
    }

    /**
     * Set Order Notes
     * @param notes
     */
    public void setNotes(OrderNotes notes) {
        this.notes = notes;
    }
}
