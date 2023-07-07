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
package io.fusion.air.microservice.domain.statemachine;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public enum OrderState {

    ORDER_INITIALIZED,

    CREDIT_CHOICE,

    CREDIT_CHECKING,

    CREDIT_APPROVED,

    CREDIT_DENIED,

    PAYMENT_PROCESSING,

    PAYMENT_CONFIRMED,

    PAYMENT_DECLINED,

    PACKING_IN_PROCESS,

    READY_FOR_SHIPMENT,

    SHIPPED,

    IN_TRANSIT,

    REACHED_DESTINATION,

    CANCELLED,

    RETURNED,

    DELIVERED;

    // Lookup table
    private static final Map<String, OrderState> lookup = new HashMap<>();

    // Populate the lookup table on loading time
    static {
        for (OrderState os : OrderState.values()) {
            lookup.put(os.name().toLowerCase(), os);
        }
    }

    // This method can be used for reverse lookup purpose
    public static OrderState fromString(String state) {
        OrderState foundState = lookup.get(state.trim().toLowerCase());
        if (foundState == null) {
            throw new IllegalArgumentException("No OrderState with text " + state + " found");
        }
        return foundState;
    }

}
