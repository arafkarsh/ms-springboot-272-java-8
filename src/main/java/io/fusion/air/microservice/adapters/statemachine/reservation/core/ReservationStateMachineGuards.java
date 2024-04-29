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
package io.fusion.air.microservice.adapters.statemachine.reservation.core;

import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationConstants;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation State Machine Guards
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class ReservationStateMachineGuards {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    @Bean
    public Guard<ReservationState, ReservationEvent> checkReservationValidity() {
        return context -> {
            // Extract ReservationEntity from the Extended State
            ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
            if(reservation != null) {
                log.info("Reservation Reservation Validity = " + reservation.isValidReservations());
                // Returns TRUE if the Reservation is valid
                return reservation.isValidReservations();
            }
            // Invalid Reservation - Reservation Terminated
            return false;
        };
    }

    @Bean
    public Guard<ReservationState, ReservationEvent> doHotelBooking() {
        return context -> {
            // Extract ReservationEntity from the Extended State
            ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
            if(reservation != null) {
                log.info("Reservation Hotel Booking = " + reservation.isHotelReservationRequired());
                // Returns TRUE if the Hotel Reservation is valid
                return reservation.isHotelReservationRequired();
            }
            // Hotel Booking Not Required
            return false;
        };
    }

    @Bean
    public Guard<ReservationState, ReservationEvent> doRentalBooking() {
        return context -> {
            // Extract ReservationEntity from the Extended State
            ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
            if(reservation != null) {
                log.info("Reservation Rental Booking = " + reservation.isRentalReservationRequired());
                // Returns TRUE if the Rental Reservation is valid
                return reservation.isRentalReservationRequired();
            }
            // Rental Booking Not Required
            return false;
        };
    }

    @Bean
    public Guard<ReservationState, ReservationEvent> doFlightBooking() {
        return context -> {
            // Extract ReservationEntity from the Extended State
            ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
            if(reservation != null) {
                log.info("Reservation Flight Booking = " + reservation.isFlightReservationRequired());
                // Returns TRUE if the Flight Reservation is valid
                return reservation.isFlightReservationRequired();
            }
            // Flight Booking Not Required
            return false;
        };
    }

    @Bean
    public Guard<ReservationState, ReservationEvent> isAllAcksReceived() {
        return context -> {
            System.out.println("[][][][][][][] ========== GUARD >> isAllAcksReceived()");
            // Extract ReservationEntity from the Extended State
            ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
            if(reservation != null) {
                log.info("Reservation ALL ACKS Received = " + reservation.isAllAcksReceived());
                // Returns TRUE if the Flight Reservation is valid
                return reservation.isAllAcksReceived();
            }
            // Flight Booking Not Required
            return false;
        };
    }
}
