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
package io.fusion.air.microservice.domain.entities.reservation;
// Custom
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fusion.air.microservice.domain.entities.core.springdata.AbstractBaseEntityWithUUID;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationNotes;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
import io.fusion.air.microservice.utils.Utils;

import javax.persistence.*;

/**
 * To Keep Track of Order States and its Transitions based on Order Event
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "reservation_state_history_tx")
public class ReservationStateHistoryEntity extends AbstractBaseEntityWithUUID implements Comparable<ReservationStateHistoryEntity> {

    @Column(name = "sourceState")
    @Enumerated(EnumType.STRING)
    private ReservationState sourceState;

    @Column(name = "targetState")
    @Enumerated(EnumType.STRING)
    private ReservationState targetState;

    @Column(name = "transitionEvent")
    @Enumerated(EnumType.STRING)
    private ReservationEvent transitionEvent;

    @Column(name= "aggregateVersionNo", columnDefinition = "int default 0")
    private Integer aggregateVersionNo;

    @Column(name = "notes")
    private String notes;

    public ReservationStateHistoryEntity() {}

    /**
     * Create Order State History
     * @param _source
     * @param _target
     * @param _event
     * @param _version
     * @param _notes
     */
    public ReservationStateHistoryEntity(ReservationState _source, ReservationState _target, ReservationEvent _event,
                                         int _version, String _notes) {
        this.sourceState = _source;
        this.targetState = _target;
        this.transitionEvent = _event;
        this.aggregateVersionNo = _version;
        this.notes = _notes;
    }

    /**
     * Returns Source State
     * @return
     */
    public ReservationState getSourceState() {
        return sourceState;
    }

    /**
     * Returns Target State
     * @return
     */
    public ReservationState getTargetState() {
        return targetState;
    }

    /**
     * Returns Transition Event
     * @return
     */
    public ReservationEvent getTransitionEvent() {
        return transitionEvent;
    }

    /**
     * Get the Order Version
     * @return
     */
    public int getAggregateVersionNo() {
        return aggregateVersionNo;
    }

    /**
     * Returns Notes
     * @return
     */
    @JsonIgnore
    public String getNotesString() {
        return notes;
    }

    /**
     * Returns Object Error
     * @return
     */
    public ReservationNotes getNotes() {
        if(notes != null && notes.trim().length() > 0) {
            try {
                return Utils.fromJsonToObject(notes, ReservationNotes.class);
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
        return null;
    }

    /**
     * Compare based on Order Version For Sorting in Ascending order.
     */
    @Override
    @JsonIgnore
    public int compareTo(ReservationStateHistoryEntity o) {
        return this.aggregateVersionNo - o.aggregateVersionNo;
    }
}
