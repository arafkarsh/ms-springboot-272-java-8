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
import io.fusion.air.microservice.domain.models.reservation.RentalType;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */

@Entity
@Table(name = "reservation_rental_tx")
public class RentalReservationEntity extends AbstractBaseEntityWithUUID {

    @Column(name = "primaryDriver")
    private String primaryDriver;

    @Column(name = "rentalId")
    private String rentalId;

    @Column(name = "rentalName")
    private String rentalName;

    @Column(name = "rentalType")
    @Enumerated(EnumType.STRING)
    private RentalType rentalType;

    @Column(name = "days")
    private Integer days;

    @Column(name ="startDate")
    private LocalDate startDate;

    @Column(name ="endDate")
    private LocalDate endDate;

    @Column(name = "rentalVehicleLicense")
    private String rentalVehicleLicense;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "reservationStatus")
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    @Column(name = "statusReasons")
    private String statusReasons;

    @Column(name = "rentalReservationNo")
    private String rentalReservationNo;

    public RentalReservationEntity() {}

    /**
     * Create Rental Reservation
     *
     * @param _rentalId
     * @param _rentalName
     * @param _days
     * @param _rate
     */
    public RentalReservationEntity(String _rentalId, String _rentalName, Integer _days, Integer _rate) {
        this.rentalId = _rentalId;
        this.rentalName = _rentalName;
        this.days = _days;
        this.rate = _rate;
    }

    /**
     * Returns the Primary Driver Name
     * @return
     */
    public String getPrimaryDriver() {
        return primaryDriver;
    }

    /**
     * Returns Rental ID
     * @return
     */
    public String getRentalId() {
        return rentalId;
    }

    /**
     * Rental Type
     * @return
     */
    public RentalType getRentalType() {
        return rentalType;
    }

    /**
     * Returns Rental Name
     * @return
     */
    public String getRentalName() {
        return rentalName;
    }

    /**
     * Returns Days
     * @return
     */
    public Integer getDays() {
        return days;
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
        return days * rate;
    }

    /**
     * Rental Start Date
     * @return
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Rental End Date
     * @return
     */
    public LocalDate getEndDate() {
        return endDate;
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
     * Returns Rental Vehicle License No.
     * @return
     */
    public String getRentalVehicleLicense() {
        return rentalVehicleLicense;
    }

    /**
     * Returns Rental Reservation No.
     * @return
     */
    public String getRentalReservationNo() {
        return rentalReservationNo;
    }
}
