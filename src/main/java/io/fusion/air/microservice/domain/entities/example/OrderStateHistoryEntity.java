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
package io.fusion.air.microservice.domain.entities.example;

import io.fusion.air.microservice.domain.entities.core.springdata.AbstractBaseEntityWithUUID;
import io.fusion.air.microservice.domain.statemachine.OrderEvent;
import io.fusion.air.microservice.domain.statemachine.OrderState;

import javax.persistence.*;

/**
 * To Keep Track of Order States and its Transitions based on Order Event
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "order_state_history_tx")
public class OrderStateHistoryEntity extends AbstractBaseEntityWithUUID {

    @Column(name = "sourceState")
    @Enumerated(EnumType.STRING)
    private OrderState sourceState;

    @Column(name = "targetState")
    @Enumerated(EnumType.STRING)
    private OrderState targetState;

    @Column(name = "transitionEvent")
    @Enumerated(EnumType.STRING)
    private OrderEvent transitionEvent;

    @Column(name = "notes")
    private String notes;

    public OrderStateHistoryEntity() {}

    /**
     * Create Order State History
     * @param _source
     * @param _target
     * @param _event
     * @param _notes
     */
    public OrderStateHistoryEntity(OrderState _source, OrderState _target, OrderEvent _event, String _notes) {
        this.sourceState = _source;
        this.targetState = _target;
        this.transitionEvent = _event;
        this.notes = _notes;
    }

    /**
     * Returns Source State
     * @return
     */
    public OrderState getSourceState() {
        return sourceState;
    }

    /**
     * Returns Target State
     * @return
     */
    public OrderState getTargetState() {
        return targetState;
    }

    /**
     * Returns Transition Event
     * @return
     */
    public OrderEvent getTransitionEvent() {
        return transitionEvent;
    }

    /**
     * Returns Notes
     * @return
     */
    public String getNotes() {
        return notes;
    }
}
