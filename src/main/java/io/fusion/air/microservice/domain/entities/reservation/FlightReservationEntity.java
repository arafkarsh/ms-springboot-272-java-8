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
package io.fusion.air.microservice.domain.entities.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fusion.air.microservice.domain.entities.core.springdata.AbstractBaseEntityWithUUID;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "reservation_flight_tx")
public class FlightReservationEntity extends AbstractBaseEntityWithUUID {

    @Column(name = "passengerName")
    private String passengerName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "flightId")
    private String flightId;

    @Column(name = "airlines")
    private String airlines;

    @Column(name = "fromCity")
    private String fromCity;

    @Column(name = "toCity")
    private String toCity;

    @Column(name ="journeyDate")
    private LocalDate journeyDate;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "flightReservationNo")
    private String flightReservationNo;

    @Column(name = "pnr")
    private String pnr;

    @Column(name = "reservationStatus")
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    @Column(name = "statusReasons")
    private String statusReasons;

    public FlightReservationEntity() {}

    /**
     * Create Rental Reservation
     *
     * @param _rentalId
     * @param _rentalName
     * @param _days
     * @param _rate
     */
    public FlightReservationEntity(String _rentalId, String _rentalName, Integer _rate) {
        this.flightId = _rentalId;
        this.airlines = _rentalName;
        this.rate = _rate;
    }

    /**
     * Returns the Passenger Name
     * @return
     */
    public String getPassengerName() {
        return passengerName;
    }

    /**
     * Returns Gender
     * @return
     */
    public String getGender() {
        return gender;
    }

    /**
     * Returns Rental ID
     * @return
     */
    public String getFlightId() {
        return flightId;
    }

    /**
     * Returns Rental Name
     * @return
     */
    public String getAirlines() {
        return airlines;
    }

    /**
     * Returns the Rate
     * @return
     */
    public Integer getRate() {
        return rate;
    }

    /**
     * Returns the Total Cost
     * @return
     */
    public Integer getTotalCost() {
        return rate;
    }

    /**
     * Rental Start Date
     * @return
     */
    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    /**
     * Rental Reservation Status
     * @return
     */
    public ReservationState getReservationState() {
        return reservationState;
    }

    /**
     * Returns the Status Reasons if ANY
     * @return
     */
    public String getStatusReasons() {
        return statusReasons;
    }

    /**
     * From City
     * @return
     */
    public String getFromCity() {
        return fromCity;
    }

    /**
     * Returns to City
     * @return
     */
    public String getToCity() {
        return toCity;
    }

    /**
     * Returns Flight Reservation No.
     * @return
     */
    public String getFlightReservationNo() {
        return flightReservationNo;
    }

    /**
     * Get PNR
     * @return
     */
    public String getPnr() {
        return pnr;
    }
}
