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
package io.fusion.air.microservice.adapters.service;
// Custom
import io.fusion.air.microservice.adapters.repository.ReservationRepository;
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.exceptions.BusinessServiceException;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import io.fusion.air.microservice.domain.exceptions.DatabaseException;
import io.fusion.air.microservice.domain.exceptions.InputDataException;
import io.fusion.air.microservice.domain.ports.services.ReservationService;
import io.fusion.air.microservice.domain.ports.services.ReservationStateMachineService;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.utils.Utils;
// Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
// Java
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation Service
 * Reservation Processing is implemented with Spring State Machine
 *
 * 1. Request for Hotel Booking
 * 2. Request for Rental Booking
 * 3. Request for Flight Booking
 * 4. Request for Payment
 * 5. Request to Send Invoices and Travel Plans
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
@RequestScope
public class ReservationServiceImpl implements ReservationService {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationStateMachineService reservationStateMachineService;

    /**
     * ONLY FOR TESTING PURPOSE
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationEntity> findAll() {
        try {
            return (List<ReservationEntity>) reservationRepository.findAll();
        } catch(Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error retrieving data!", e);
        }
    }

    /**
     * Find Order by Customer ID
     *
     * @param customerId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationEntity> findByCustomerId(String customerId) {
        try {
            return reservationRepository.findByCustomerId(customerId);
        } catch(Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Error retrieving data!", e);
        }
    }

    /**
     * Find by reservation by Customer ID and reservation ID
     *
     * @param customerId
     * @param reservationId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationEntity> findById(String customerId, String reservationId) {
        Optional<ReservationEntity> o = reservationRepository.findByCustomerIdAndReservationId(customerId, Utils.getUUID(reservationId));
        if(o.isPresent()) {
            return o;
        }
        throw new DataNotFoundException("reservation Not Found for reservationId="+reservationId);
    }

    /**
     * Find By reservation by Customer ID and reservation ID
     *
     * @param customerId
     * @param reservationId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationEntity> findById(String customerId, UUID reservationId) {
        Optional<ReservationEntity> o = reservationRepository.findByCustomerIdAndReservationId(customerId, reservationId);
        if(o.isPresent()) {
            return o;
        }
        throw new DataNotFoundException("reservation Not Found for reservationId="+reservationId);    }

    /**
     * Save Reservation
     *
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public ReservationEntity save(ReservationEntity reservation) {
        if(reservation == null) {
            throw new InputDataException("Invalid reservation Data");
        }
        reservation.calculateTotalValue();
        return reservationRepository.save(reservation);
    }

    /**
     * Reset the reservation State to Initialized
     * THIS METHOD IS ONLY FOR TESTING THE STATE MACHINE BY RESETTING THE reservation BACK TO ITS INIT STATE.
     * @param customerId
     * @param reservationId
     * @return
     */
    @Transactional
    public ReservationEntity resetReservation(String customerId, String reservationId) {
        Optional<ReservationEntity> reservationOpt = findById( customerId,  reservationId);
        log.info("Reset reservation ID = "+reservationId);
        if(reservationOpt.isPresent()) {
            ReservationEntity reservation = reservationOpt.get();
            reservation.resetState();
            reservationRepository.save(reservation);
            return reservation;
        }
        throw new DataNotFoundException("reservation Not Found for "+reservationId);
    }


    /**
     * For Testing Purpose Only
     * Handle Event - Generic Method
     *
     * @param customerId
     * @param reservationId
     * @param event
     * @return
     */
    public ReservationEntity handleEvent(String customerId, String reservationId, String event) {
        ReservationEvent reservationEvent = ReservationEvent.fromString(event);
        return handleEvent(customerId, reservationId, reservationEvent);
    }

    /**
     * For Testing Purpose Only
     * Handle Event - Generic Method
     *
     * @param customerId
     * @param reservationId
     * @param reservationEvent
     * @return
     */
    public ReservationEntity handleEvent(String customerId, String reservationId, ReservationEvent reservationEvent) {
        if(reservationEvent == null) {
            throw new BusinessServiceException("Invalid Event for Reservation Processing!");
        }
        Optional<ReservationEntity> reservationOpt = findById( customerId,  reservationId);
        log.info("Handle Event "+reservationEvent+" For reservation ID = "+reservationId);
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("(1) INCOMING EVENT == (reservationServiceImpl) === ["+reservationEvent.name()+"] ======= >> reservationId = "+reservationId);
        System.out.println("--------------------------------------------------------------------------------------------------");

        ReservationEntity reservation = reservationOpt.get();
        // Send Event to the State Machine
        reservationStateMachineService.sendEvent(reservationEvent, reservation);

        return reservation;
    }
}
