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
public enum ReservationResult {

    IN_PROGRESS,
    SUSPENDED,
    INVALID_REQUEST,

    ROLLBACK,
    // HOTEL BOOKING RESULT
    HOTEL_BOOKING_CONFIRMED,
    HOTEL_BOOKING_DECLINED,
    HOTEL_BOOKING_ROLLBACK,

    // RENTAL BOOKING RESULT
    RENTAL_BOOKING_CONFIRMED,
    RENTAL_BOOKING_DECLINED,
    RENTAL_BOOKING_ROLLBACK,

    // FLIGHT BOOKING RESULT
    FLIGHT_BOOKING_CONFIRMED,
    FLIGHT_BOOKING_DECLINED,
    FLIGHT_BOOKING_ROLLBACK,

    // PAYMENT RESULT
    PAYMENT_APPROVED,
    PAYMENT_DECLINED,

    // TRIP BOOKING RESULT
    TRIP_CONFIRMED,
    TRIP_CANCELLED;

    // Lookup table
    private static final Map<String, ReservationResult> lookup = new HashMap<>();

    // Populate the lookup table on loading time
    static {
        for (ReservationResult os : ReservationResult.values()) {
            lookup.put(os.name().toLowerCase(), os);
        }
    }

    // This method can be used for reverse lookup purpose
    public static ReservationResult fromString(String state) {
        return lookup.get(state.trim().toLowerCase());
    }

}
