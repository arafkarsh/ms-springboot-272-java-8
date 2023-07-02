package io.fusion.air.microservice.domain.statemachine;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public enum OrderStates {

    ORDER_PLACED,

    CREDIT_CHECKING,

    PAYMENT_PROCESSING,

    PAYMENT_CONFIRMED,

    PACKING_IN_PROCESS,

    READY_FOR_SHIPMENT,

    SHIPPED,

    IN_TRANSIT,

    REACHED_DESTINATION,

    DELIVERED,

    RETURNED,

    CANCELLED

}
