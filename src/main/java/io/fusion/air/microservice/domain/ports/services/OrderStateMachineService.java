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
import io.fusion.air.microservice.domain.entities.example.OrderEntity;

/**
 * Order State Machine Service
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface OrderStateMachineService {

    /**
     * Request for Credit Approval
     *
     * @param order
     * @return
     */
    public void requestCreditApproval(OrderEntity order);

    /**
     * Credit Approved
     * @param order
     */
    public void creditApproved(OrderEntity order);

    /**
     * Credit Declined
     * @param order
     */
    public void creditDeclined(OrderEntity order);

    /**
     * Process the Payment
     * @param order
     * @return
     */
    public void processPayment(OrderEntity order);

    /**
     * Payment Approved
     * @param order
     * @return
     */
    public void paymentApproved(OrderEntity order);

    /**
     * Payment Declined
     * @param order
     * @return
     */
    public void paymentDeclined(OrderEntity order);

    /**
     * Ship the Product
     * @param order
     * @return
     */
    public void shipTheProduct(OrderEntity order);

    /**
     * Send For Delivery
     * @param order
     * @return
     */
    public void sendForDelivery(OrderEntity order);

    /**
     * Order Delivered
     * @param order
     */
    public void orderDelivered(OrderEntity order);

    /**
     * Order Returned
     * @param order
     */
    public void orderReturned(OrderEntity order);
    /**
     * Cancel the Order
     * @param order
     * @return
     */
    public void orderCancelled(OrderEntity order);

}
