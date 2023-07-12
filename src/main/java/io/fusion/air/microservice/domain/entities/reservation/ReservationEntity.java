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
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationResult;
import io.fusion.air.microservice.domain.statemachine.reservation.ReservationState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Reservation Entity
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Entity
@Table(name = "reservation_tx")
public class ReservationEntity extends AbstractBaseEntityWithUUID {

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "totalValue")
    private Integer totalValue;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")
    private List<HotelReservationEntity> hotelReservationList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")
    private List<RentalReservationEntity> rentalReservationList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")
    private List<FlightReservationEntity> flightReservationList = new ArrayList<>();

    @Embedded
    private CustomerAddress customerAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "uuid")
    private ReservationPaymentEntity payment;

    @Column(name = "reservationStatus")
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    private ReservationResult result;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reservation_id")
    private List<ReservationStateHistoryEntity> reservationHistory = new ArrayList<>();

    private ReservationEntity() {
    }

    /**
     * Calculate the Order Value
     * @return
     */
    @JsonIgnore
    public Integer calculateTotalValue() {
        /**
        if(hotelReservations!= null) {
            for(HotelReservationEntity i : hotelReservations) {
                totalValue += i.getTotalCost();
            }
        }
         */
        totalValue += Optional.ofNullable(hotelReservationList)
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(HotelReservationEntity::getTotalCost)
                .sum();

        totalValue += Optional.ofNullable(rentalReservationList)
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(RentalReservationEntity::getTotalCost)
                .sum();

        totalValue += Optional.ofNullable(flightReservationList)
                .orElse(Collections.emptyList())
                .stream()
                .mapToInt(FlightReservationEntity::getTotalCost)
                .sum();

        System.out.println("Reservation Customer ID = "+ getCustomerId() +" Total Value = "+ totalValue);
        return totalValue;
    }

    /**
     * Sets the Order State
     * @param state
     */
    public void setState(ReservationState state) {
        reservationState = state;
    }

    /**
     * Set the Order Result
     * @param _result
     */
    public void setOrderResult(ReservationResult _result) {
        this.result = _result;
    }

    /**
     * Returns the Order ID
     * @return
     */
    public String getOrderId() {
        return super.getIdAsString();
    }

    /**
     * Get the Customer ID
     * @return
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Get the Currency
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Get the Total Order Value
     * @return
     */
    public Integer getTotalValue() {
        return totalValue;
    }

    /**
     * Get the Order Items
     * @return
     */
    public List<HotelReservationEntity> getHotelReservationList() {
        return hotelReservationList;
    }

    /**
     * Get the shipping Address
     * @return
     */
    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Get the Payment details
     * @return
     */
    public ReservationPaymentEntity getPayment() {
        return payment;
    }

    /**
     * Returns the Status of the Order
     * @return
     */
    public ReservationState getReservationState() {
        return reservationState;
    }

    /**
     * Returns the Result of the Current Order Processing State
     * @return
     */
    public ReservationResult getResult() {
        return result;
    }

    /**
     * Returns the Order State Transition History
     * @return
     */
    public List<ReservationStateHistoryEntity> getReservationHistory() {
         Collections.sort(reservationHistory);
        return reservationHistory;
    }

    /**
     * Add Order History
     * @param history
     */
    public void addReservationStateHistory(ReservationStateHistoryEntity history) {
        reservationHistory.add(history);
    }

    /**
     * ONLY TO DEMO/TEST VARIOUS DOMAIN EVENTS
     */
    public void resetState() {
        initializeOrder();
        reservationHistory.clear();
    }

    /**
     * Initialize the Order
     * @return
     */
    @JsonIgnore
    protected void initializeOrder() {
        reservationState = ReservationState.ORDER_INITIALIZED;
        result = ReservationResult.IN_PROGRESS;
    }

    /**
     * Returns Rental Reservations
     * @return
     */
    public List<RentalReservationEntity> getRentalReservationList() {
        return rentalReservationList;
    }

    /**
     * Returns Flight Reservations
     * @return
     */
    public List<FlightReservationEntity> getFlightReservationList() {
        return flightReservationList;
    }

    /**
     * Returns the Reservation Builder
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ReservationEntity reservation;

        private Builder() {
            reservation = new ReservationEntity();
            reservation.initializeOrder();
        }

        public Builder addCustomerId(String customerId) {
            reservation.customerId = customerId;
            return this;
        }

        public Builder addHotelReservations(List<HotelReservationEntity> hotelReservations) {
            reservation.hotelReservationList = hotelReservations;
            reservation.calculateTotalValue();
            return this;
        }

        public Builder addHotelReservations(HotelReservationEntity hotelReservations) {
            reservation.hotelReservationList.add(hotelReservations);
            reservation.calculateTotalValue();
            return this;
        }

        public Builder addRentalReservations(List<RentalReservationEntity> rentalReservations) {
            reservation.rentalReservationList = rentalReservations;
            reservation.calculateTotalValue();
            return this;
        }

        public Builder addRentalReservations(RentalReservationEntity rentalReservations) {
            reservation.getRentalReservationList().add(rentalReservations);
            reservation.calculateTotalValue();
            return this;
        }

        public Builder addFlightReservations(List<FlightReservationEntity> flightReservations) {
            reservation.flightReservationList = flightReservations;
            reservation.calculateTotalValue();
            return this;
        }

        public Builder addFlightReservations(FlightReservationEntity flightReservations) {
            reservation.getFlightReservationList().add(flightReservations);
            reservation.calculateTotalValue();
            return this;
        }

        public Builder addCustomerAddress(CustomerAddress customerAddress) {
            reservation.customerAddress = customerAddress;
            return this;
        }

        public Builder addPayment(ReservationPaymentEntity payment) {
            reservation.payment = payment;
            return this;
        }

        public ReservationEntity build() {
            return reservation;
        }
    }
}
