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
public enum ReservationEvent {

    AUTO_TRANSITION_EVENT,

    START_ROLLBACK_EVENT,
    ROLLBACK_EVENT,

    ROLLBACK_COMPLETE_EVENT,

    RESERVATION_VALIDATION_EVENT,
    RESERVATION_INIT_EVENT,

    // HOTEL
    HOTEL_BOOKING_REQUEST_EVENT,

    HOTEL_BOOKING_CONFIRMED_EVENT,
    HOTEL_BOOKING_DECLINED_EVENT,
    HOTEL_ROLLBACK_EVENT,
    HOTEL_BOOKING_CANCELLED_EVENT,
    HOTEL_BOOKING_COMPLETED_EVENT,
    HOTEL_ROLLBACK_ACK_EVENT,


    // RENTAL
    RENTAL_BOOKING_REQUEST_EVENT,
    RENTAL_BOOKING_CONFIRMED_EVENT,
    RENTAL_BOOKING_DECLINED_EVENT,
    RENTAL_ROLLBACK_EVENT,
    RENTAL_BOOKING_CANCELLED_EVENT,
    RENTAL_BOOKING_COMPLETED_EVENT,
    RENTAL_ROLLBACK_ACK_EVENT,


    // FLIGHT
    FLIGHT_BOOKING_REQUEST_EVENT,
    FLIGHT_BOOKING_CONFIRMED_EVENT,
    FLIGHT_BOOKING_DECLINED_EVENT,
    FLIGHT_ROLLBACK_EVENT,
    FLIGHT_BOOKING_CANCELLED_EVENT,
    FLIGHT_BOOKING_COMPLETED_EVENT,
    FLIGHT_ROLLBACK_ACK_EVENT,


    // PAYMENT
    PAYMENT_REQUEST_EVENT,
    PAYMENT_CONFIRMED_EVENT,
    PAYMENT_DECLINED_EVENT,

    SEND_INVOICE_EVENT,
    SEND_TRAVEL_DETAILS_EVENT,

    TRIP_CONFIRMED_EVENT,

    TRIP_CANCELLED_EVENT,


    FAILURE_EVENT
    ;

    // Lookup table
    private static final Map<String, ReservationEvent> lookup = new HashMap<>();

    // Populate the lookup table on loading time
    static {
        for (ReservationEvent os : ReservationEvent.values()) {
            lookup.put(os.name().toLowerCase(), os);
        }
    }

    public static ReservationEvent fromString(String event) {
        ReservationEvent foundState = lookup.get(event.trim().toLowerCase());
        if (foundState == null) {
            throw new IllegalArgumentException("No ReservationEvent with text " + event + " found");
        }
        return foundState;
    }

}
