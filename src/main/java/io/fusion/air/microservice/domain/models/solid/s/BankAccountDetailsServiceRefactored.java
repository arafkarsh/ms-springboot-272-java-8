/**
 * (C) Copyright 2024 Araf Karsh Hamid
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
package io.fusion.air.microservice.domain.models.solid.s;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * SOLID Examples
 *
 * Single Responsibility Principle (SRP)
 * A class should have only one reason to change, meaning it should have only one j
 * ob or responsibility.
 *
 * Handle all Bank Account related Activities
 *
 * The refactored Code has 1 Responsibility
 *      1. Deposit and Withdrawal
 *
 * The following responsibilities moved to respective classes
 *      2. Save Account Details
 *      3. Send Notification Details
 *
 * With Single Responsibility Pattern the Handling of Save Account Details
 * and sending Notification must be done in separate classes.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class BankAccountDetailsServiceRefactored {

    private double accountBalance;
    private String accountNumber;
    private String accountType;

    @Autowired
    private BankDetailsRepository repository;
    @Autowired
    private BankDetailsNotificationService notificationService;

    /**
     * Bank Account Details Service
     * @param _accountBalance
     * @param _accountNumber
     * @param _accountType
     */
    public BankAccountDetailsServiceRefactored(double _accountBalance,
                                               String _accountNumber,  String _accountType) {
        accountBalance  = _accountBalance;
        accountNumber  = _accountNumber;
        accountType     = _accountType;
    }

    /**
     * Deposit Amount
     * @param _depositAmount
     */
    public void deposit(double _depositAmount) {
        // Code to handle Deposit
    }

    /**
     * Withdraw Amount
     * @param _withdrawalAmount
     */
    public void withdrawal(double _withdrawalAmount) {
        // Code to handle Withdrawal
    }

    /**
     * Calculate Interest
     */
    public void calculateInterest() {
        // Code to calculate interest
    }

    /**
     * Save Bank Account Details
     * @param accountDetails
     */
    public void saveAccountDetails(Object accountDetails) {
        repository.saveBankDetails(accountDetails);
    }

    /**
     * Send Notification
     * @param notification
     */
    public void sendNotification(String notification) {
        notificationService.sendNotification(notification);
    }
}
