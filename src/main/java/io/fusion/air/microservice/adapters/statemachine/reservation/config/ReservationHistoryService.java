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
import io.fusion.air.microservice.adapters.repository.ReservationRepository;
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.entities.reservation.ReservationStateHistoryEntity;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationNotes;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationResult;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
import io.fusion.air.microservice.utils.Utils;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
// Java
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
@RequestScope
public class ReservationHistoryService {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationStateDetails reservationStateDetails;

    @Transactional
    public void saveReservationHistory(ReservationStateDetails osd) {
        ReservationState source = osd.getValue(ReservationStateDetails.SOURCE);
        ReservationState target = osd.getValue(ReservationStateDetails.TARGET);
        ReservationEvent event =  osd.getEvent();
        ReservationEntity order = osd.getReservation();
        ReservationNotes notes = osd.getNotes();
        saveReservationHistory(source, target, event, order, notes);
    }

    @Transactional
    public void saveReservationHistory(ReservationState source, ReservationState target) {
        if(reservationStateDetails.getReservation().getReservationState().getRank() < target.getRank()) {
            reservationStateDetails.addProperties(ReservationStateDetails.SOURCE, source);
            reservationStateDetails.addProperties(ReservationStateDetails.TARGET, target);
            ReservationEvent event = reservationStateDetails.getEvent();
            ReservationEntity order = reservationStateDetails.getReservation();
            ReservationNotes notes = reservationStateDetails.getNotes();
            saveReservationHistory(source, target, event, order, notes);
        } else {
            log.info("STATE ALREADY SAVED!");
        }
    }

    @Transactional
    public void saveReservationHistory(ReservationState source, ReservationState target, ReservationEvent event,
                                       ReservationEntity order, ReservationNotes errorObj) {
        if(!validateInputs(source, target, event, order)) {
            return;
        }
        if(source.name().equalsIgnoreCase(target.name())) {
            log.info("NOT SAVING: Source {} and Target {} same!", source.name(), target.name());
            return;
        }
        ReservationResult result = null;
        String notes = "";
        // If the Event is a FAILURE Event thrown by Exceptions
        if(event != null && event.equals(ReservationEvent.FAILURE_EVENT)) {
            notes = Utils.toJsonString(errorObj);
            result = ReservationResult.fromString(errorObj.getTargetState());
        }
        // Check if there are any Results Available based on Target State
        if(result == null) {
            result = ReservationResult.fromString(target.name());
        }
        ReservationStateHistoryEntity history = new ReservationStateHistoryEntity(source, target, event, order.getVersion() + 1, notes);
        order.addReservationStateHistory(history);
        order.setState(target);
        // If Results Available then Set the Result in Reservation Object
        if(result != null) {
            order.setReservationResult(result);
        }
        // Save the Reservation with the History Details
        if(target.getRank() >= order.getReservationState().getRank()) {
            reservationRepository.save(order);
            reservationStateDetails.stateSaved();
        } else {
            log.info("Reservation HISTORY NOT SAVED TARGET RANK IS {} < {} Reservation STATE RANK! ", target.getRank(),order.getReservationState().getRank());
        }
    }

    private boolean validateInputs(ReservationState source, ReservationState target, ReservationEvent event, ReservationEntity order) {
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
            log.error("Invalid (NULL) Reservation!");
            status = false;
        }
        return status;
    }
}
