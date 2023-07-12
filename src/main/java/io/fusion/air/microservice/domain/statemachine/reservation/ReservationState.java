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
package io.fusion.air.microservice.domain.statemachine.reservation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public enum ReservationState {

    RESERVATION_REQUEST_RECEIVED(1),
    IN_PROGRESS(2),
    RESERVATION_COMPLETED(200),
    ERROR(100),
    SUSPENDED(99),
    RESERVATION_INITIALIZED (10),

    // RESERVATION CHOICES
    HOTEL_CHOICE(11),
    RENTAL_CHOICE(21),
    FLIGHT_CHOICE(31),

    // HOTEL BOOKING STATES
    HOTEL_BOOKING_REQUEST(12),
    HOTEL_BOOKING_CONFIRMED(13),
    HOTEL_BOOKING_DECLINED(14),
    HOTEL_BOOKING_ROLLBACK(15),

    // RENTAL BOOKING STATES
    RENTAL_BOOKING_REQUEST(21),
    RENTAL_BOOKING_CONFIRMED(22),
    RENTAL_BOOKING_DECLINED(23),
    RENTAL_BOOKING_ROLLBACK(24),

    // FLIGHT BOOKING STATES
    FLIGHT_BOOKING_REQUEST(31),
    FLIGHT_BOOKING_CONFIRMED(32),
    FLIGHT_BOOKING_DECLINED(33),
    FLIGHT_BOOKING_ROLLBACK(34),

    // PAYMENT STATES

    PAYMENT_REQUEST_INIT(41),
    PAYMENT_APPROVED(42),
    PAYMENT_DECLINED(43),

    // INVOICE AND TRIP DETAILS
    SEND_INVOICE_START(51),
    SEND_INVOICE_END(52),
    SEND_TRAVEL_DETAILS_START(61),
    SEND_TRAVEL_DETAILS_DONE(62),

    // TRIP STATUS
    TRIP_CONFIRMED(71),
    TRIP_CANCELLED(71);

    private final int rank;

    ReservationState(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return this.rank;
    }

    // Lookup table
    private static final Map<String, ReservationState> lookup = new HashMap<>();

    // Populate the lookup table on loading time
    static {
        for (ReservationState os : ReservationState.values()) {
            lookup.put(os.name().toLowerCase(), os);
        }
    }

    // This method can be used for reverse lookup purpose
    public static ReservationState fromString(String state) {
        ReservationState foundState = lookup.get(state.trim().toLowerCase());
        if (foundState == null) {
            throw new IllegalArgumentException("No OrderState with text " + state + " found");
        }
        return foundState;
    }

}
