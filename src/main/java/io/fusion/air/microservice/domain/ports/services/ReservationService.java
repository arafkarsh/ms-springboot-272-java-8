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
package io.fusion.air.microservice.domain.ports.services;
// Custom
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Reservation Service
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface ReservationService {

    /**
     * ONLY FOR TESTING PURPOSE
     * @return
     */
    public List<ReservationEntity> findAll();

    /**
     * Find Reservation by Customer ID
     * @param customerId
     * @return
     */
    public List<ReservationEntity> findByCustomerId(String customerId);

    /**
     * Find by Reservation by Customer ID and Reservation ID
     * @param customerId
     * @param reservationId
     * @return
     */
    public Optional<ReservationEntity> findById(String customerId, String reservationId);

    /**
     * Find By Reservation by Customer ID and Reservation ID
     * @param customerId
     * @param reservationId
     * @return
     */
    public Optional<ReservationEntity> findById( String customerId, UUID reservationId);

    /**
     * Save Reservation
     * @param order
     * @return
     */
    public ReservationEntity save(ReservationEntity order);

    /**
     * Reset the Reservation State to Initialized
     * THIS METHOD IS ONLY FOR TESTING THE STATE MACHINE BY RESETTING THE RESERVATION BACK TO ITS INIT STATE.
     * @param customerId
     * @param reservationId
     * @return
     */
    public ReservationEntity resetReservation(String customerId, String reservationId);

    /**
     * For Testing Purpose Only
     * Handle Event - Generic Method
     * @param customerId
     * @param reservationId
     * @param event
     * @return
     */
    public ReservationEntity handleEvent(String customerId, String reservationId, String event);
}
