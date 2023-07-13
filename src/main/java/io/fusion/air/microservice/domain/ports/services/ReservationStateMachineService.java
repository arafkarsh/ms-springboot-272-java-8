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
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationEvent;

import java.util.List;

/**
 * Reservation State Machine Service
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface ReservationStateMachineService {

    /**
     * Check for Valid Reservation
     *
     * @param reservation
     * @return
     */
    public void reservationValidation(ReservationEntity reservation);

    /**
     * Initialize Reservation
     * @param reservation
     */
    public void reservationInit(ReservationEntity reservation);

    // Hotel Booking

    /**
     * Hotel booking request
     * @param reservation
     */
    public void hotelBookingRequest(ReservationEntity reservation);

    /**
     * Hotel Booking Confirmed
     * @param reservation
     */
    public void hotelBookingConfirmed(ReservationEntity reservation);

    /**
     * Hotel Booking declined
     * @param reservation
     */
    public void hotelBookingDeclined(ReservationEntity reservation);

    /**
     * Hotel Booking Rollback
     * @param reservation
     */
    public void hotelBookingRollback(ReservationEntity reservation);

    // Rental Booking

    /**
     * Rental booking request
     * @param reservation
     */
    public void rentalBookingRequest(ReservationEntity reservation);

    /**
     * Rental Booking Confirmed
     * @param reservation
     */
    public void rentalBookingConfirmed(ReservationEntity reservation);

    /**
     * Rental Booking declined
     * @param reservation
     */
    public void rentalBookingDeclined(ReservationEntity reservation);

    /**
     * Rental Booking Rollback
     * @param reservation
     */
    public void rentalBookingRollback(ReservationEntity reservation);

    // Flight Booking

    /**
     * Flight booking request
     * @param reservation
     */
    public void flightBookingRequest(ReservationEntity reservation);

    /**
     * Flight Booking Confirmed
     * @param reservation
     */
    public void flightBookingConfirmed(ReservationEntity reservation);

    /**
     * Flight Booking declined
     * @param reservation
     */
    public void flightBookingDeclined(ReservationEntity reservation);

    /**
     * Flight Booking Rollback
     * @param reservation
     */
    public void flightBookingRollback(ReservationEntity reservation);

    // Payment

    /**
     * Payment request
     * @param reservation
     */
    public void paymentRequest(ReservationEntity reservation);

    /**
     * Payment Confirmed
     * @param reservation
     */
    public void paymentConfirmed(ReservationEntity reservation);

    /**
     * Payment declined
     * @param reservation
     */
    public void paymentDeclined(ReservationEntity reservation);

    // Invoice and Travel Plans

    /**
     * Invoice
     * @param reservation
     */
    public void sendInvoice(ReservationEntity reservation);

    /**
     * Send Travel Plans
     * @param reservation
     */
    public void sendTravelPlans(ReservationEntity reservation);


    /**
     * Trip Confirmed
     * @param reservation
     */
    public void tripConfirmed(ReservationEntity reservation);

    /**
     * Trip Cancelled
     * @param reservation
     */
    public void tripCancelled(ReservationEntity reservation);

    /**
     * Send Message/Event to State Machine
     * @param event
     * @param reservation
     */
    public void sendEvent(ReservationEvent event, ReservationEntity reservation);

    /**
     * Sends Multiple Events to the State Machine
     * @param reservation
     * @param events
     */
    public void multipleEvents(ReservationEntity reservation, List<ReservationEvent> events);

}
