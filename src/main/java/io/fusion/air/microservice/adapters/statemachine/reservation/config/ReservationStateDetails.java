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
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationNotes;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
// Java
import java.io.Serializable;
import java.util.HashMap;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Component
@RequestScope
public class ReservationStateDetails implements Serializable {

    public static final String SOURCE = "SOURCE";
    public static final String TARGET = "TARGET";

    private ReservationEntity reservation;
    private HashMap<String, ReservationState> states = new HashMap<String, ReservationState>();

    private ReservationEvent event;

    private boolean stateSaved = false;

    private Exception exception;

    private ReservationNotes notes;

    public ReservationStateDetails() {}

    /**
     * Add Reservation
     * @param _reservation
     */
    public void addReservations(ReservationEntity _reservation) {
        reservation = _reservation;
    }

    /**
     * Get Reservation
     * @return
     */
    public ReservationEntity getReservation() {
        return reservation;
    }

    /**
     * Add Properties
     * @param key
     * @param value
     */
    public void addProperties(String key, ReservationState value) {
        states.put(key, value);
    }

    /**
     * Return Properties
     * @param key
     * @return
     */
    public ReservationState getValue(String key) {
        return states.get(key);
    }

    /**
     * Returns Event
     * @return
     */
    public ReservationEvent getEvent() {
        return event;
    }

    /**
     * Set Event
     * @param event
     */
    public void setEvent(ReservationEvent event) {
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
    public ReservationNotes getNotes() {
        return notes;
    }

    /**
     * Set Order Notes
     * @param notes
     */
    public void setNotes(ReservationNotes notes) {
        this.notes = notes;
    }
}
