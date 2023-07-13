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
// Custom
import io.fusion.air.microservice.domain.entities.reservation.ReservationEntity;
import io.fusion.air.microservice.domain.exceptions.BusinessServiceException;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationConstants;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;
// Spring
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
// Spring State Machine
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
// Java
import org.slf4j.Logger;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Reservation State Machine Actions
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Component
public class ReservationStateMachineActions {

    // Set Logger -> Lookup will automatically determine the class name.
    private static final Logger log = getLogger(lookup().lookupClass());

    // Hotel Booking

    @Bean(name = "Reservation-Hotel-Request")
    public Action<ReservationState, ReservationEvent> hotelReservationRequestAction() {
        return context -> {
            logStateTransition(context, "ACTION ON HOTEL RESERVATION REQUEST EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send To Request Using Kafka Topic: HOTEL BOOKING REQUEST
        };
    }

    @Bean(name = "Reservation-Hotel-Confirmed")
    public Action<ReservationState, ReservationEvent> hotelReservationConfirmedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON HOTEL RESERVATION CONFIRMED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for HOTEL BOOKING CONFIRMED
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean(name = "Reservation-Hotel-Declined")
    public Action<ReservationState, ReservationEvent> hotelReservationDeclinedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON HOTEL RESERVATION DECLINED EVENT - THROWS EXCEPTION");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for HOTEL BOOKING DECLINED
            throw new BusinessServiceException("Service Unavailable: HOTEL BOOKING: Target State = "+context.getTarget().getId().name());
        };
    }

    @Bean(name = "Reservation-Hotel-Rollback")
    public Action<ReservationState, ReservationEvent> hotelReservationRollbackAction() {
        return context -> {
            logStateTransition(context, "ACTION ON HOTEL RESERVATION ROLLBACK EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for HOTEL BOOKING ROLLBACK
        };
    }

    // RENTAL Booking

    @Bean(name = "Reservation-Rental-Request")
    public Action<ReservationState, ReservationEvent> rentalReservationRequestAction() {
        return context -> {
            logStateTransition(context, "ACTION ON RENTAL RESERVATION REQUEST EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send To Request Using Kafka Topic: RENTAL BOOKING REQUEST
        };
    }

    @Bean(name = "Reservation-Rental-Confirmed")
    public Action<ReservationState, ReservationEvent> rentalReservationConfirmedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON RENTAL RESERVATION CONFIRMED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for RENTAL BOOKING CONFIRMED
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean(name = "Reservation-Rental-Declined")
    public Action<ReservationState, ReservationEvent> rentalReservationDeclinedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON RENTAL RESERVATION DECLINED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for RENTAL BOOKING DECLINED
        };
    }

    @Bean(name = "Reservation-Rental-Rollback")
    public Action<ReservationState, ReservationEvent> rentalReservationRollbackAction() {
        return context -> {
            logStateTransition(context, "ACTION ON RENTAL RESERVATION ROLLBACK EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for RENTAL BOOKING ROLLBACK
        };
    }

    // FLIGHT Booking

    @Bean(name = "Reservation-flight-Request")
    public Action<ReservationState, ReservationEvent> flightReservationRequestAction() {
        return context -> {
            logStateTransition(context, "ACTION ON FLIGHT RESERVATION REQUEST EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send To Request Using Kafka Topic: FLIGHT BOOKING REQUEST
        };
    }

    @Bean(name = "Reservation-flight-Confirmed")
    public Action<ReservationState, ReservationEvent> flightReservationConfirmedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON FLIGHT RESERVATION CONFIRMED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for FLIGHT BOOKING CONFIRMED
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean(name = "Reservation-flight-declined")
    public Action<ReservationState, ReservationEvent> flightReservationDeclinedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON FLIGHT RESERVATION DECLINED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for FLIGHT BOOKING DECLINED
        };
    }

    @Bean(name = "Reservation-flight-Rollback")
    public Action<ReservationState, ReservationEvent> flightReservationRollbackAction() {
        return context -> {
            logStateTransition(context, "ACTION ON FLIGHT RESERVATION ROLLBACK EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for FLIGHT BOOKING ROLLBACK
        };
    }

    // Payment

    @Bean(name = "Reservation-Payment-Request")
    public Action<ReservationState, ReservationEvent> paymentRequestAction() {
        return context -> {
            logStateTransition(context, "ACTION ON PAYMENT REQUEST EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send To Request Using Kafka Topic: PAYMENT REQUEST
        };
    }

    @Bean(name = "Reservation-Payment-Approved")
    public Action<ReservationState, ReservationEvent> paymentConfirmedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON PAYMENT CONFIRMED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for PAYMENT CONFIRMED
        };
    }

    /**
     * Action to Demonstrate Exception handling in Spring State Machine
     * @return
     */
    @Bean(name = "Reservation-Payment-Declined")
    public Action<ReservationState, ReservationEvent> paymentDeclinedAction() {
        return context -> {
            logStateTransition(context, "ACTION ON PAYMENT DECLINED EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for PAYMENT DECLINED
        };
    }

    // Invoice

    @Bean(name = "Reservation-Send-Invoice")
    public Action<ReservationState, ReservationEvent> sendInvoiceAction() {
        return context -> {
            logStateTransition(context, "ACTION ON SEND INVOICE EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Send To Request Using Kafka Topic: SEND INVOICE
        };
    }

    @Bean(name = "Reservation-Send-Travel-Details")
    public Action<ReservationState, ReservationEvent> sendTravelDetailsAction() {
        return context -> {
            logStateTransition(context, "ACTION ON SEND TRAVEL DETAILS EVENT");
            // Add Business Logic to Handle Event
            // ...
            // Received Event from Kafka Topic for SEND TRAVEL DETAILS
        };
    }

    // AUTO TRANSITION

    @Bean(name = "Reservation-Auto-Transition")
    public Action<ReservationState, ReservationEvent> autoTransition() {
        return context -> {
            System.out.println("(6) AUTO TRANSITIONING: == (StateMachineActions) ================================================= >>");
            // Get the State Machine from the context
            StateMachine<ReservationState, ReservationEvent> stateMachine = context.getStateMachine();
            if(stateMachine != null) {
                // Extract Reservation from the Extended State
                ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
                if(reservation != null) {
                    // Create Message for the Failure Event
                    Message mesg = MessageBuilder.withPayload(ReservationEvent.AUTO_TRANSITION_EVENT)
                            .setHeader(ReservationConstants.RESERVATION_ID_HEADER, reservation.getReservationId())
                            .build();
                    // Send Event to the State Machine
                    stateMachine.sendEvent(mesg);
                    logStateTransition(context, "AUTO TRANSITION EVENT SEND: ");
                }
            } else {
                logStateTransition(context, "ERROR GETTING STATE MACHINE FROM THE CONTEXT!!!!!");
            }
        };
    }

    /**
     * Log the State Transition Details
     * @param context
     * @param _msg
     */
    private void logStateTransition(StateContext<ReservationState, ReservationEvent> context, String _msg) {
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("(3) STATE ACTION on EVENT  == (StateMachineActions) =========================================== >>");
        System.out.println("--------------------------------------------------------------------------------------------------");

        ReservationEntity reservation = context.getExtendedState().get(ReservationConstants.RESERVATION_HEADER, ReservationEntity.class);
        ReservationState source = context.getSource().getId();
        ReservationState target = context.getTarget().getId();
        ReservationEvent event = context.getEvent();
        String s = (source != null) ? source.name() : "No-Source";
        String t = (target != null) ? target.name() : "No-Target";
        String e = (event != null) ?  event.name() : "No-Event";
        String o = (reservation != null) ? reservation.getReservationId() : "No-Reservation-Found!";
        log.info("TRANSITIONING FROM [{}] TO ({}) based on EVENT = <{}>", s, t,e);
        log.info("{} for Reservation ID = {}",_msg,o);
    }
}