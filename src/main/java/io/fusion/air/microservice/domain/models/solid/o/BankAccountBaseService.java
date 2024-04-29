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
package io.fusion.air.microservice.domain.models.solid.o;

import io.fusion.air.microservice.domain.models.solid.s.BankDetailsNotificationService;
import io.fusion.air.microservice.domain.models.solid.s.BankDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SOLID Examples
 *
 * Open-Close Principle (OCP)
 * Software entities (classes, modules, functions, etc.) should be open for extension, but
 * closed for modification.
 *
 * The Bank Account is Extended with different calculations for Interest Calculations.
 * However, the different algorithms are implemented in different Sub Classes and the
 * base class has No Modifications.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public abstract class BankAccountBaseService implements BankAccountDetails {

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
    public BankAccountBaseService(double _accountBalance,
                                  String _accountNumber, String _accountType) {
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

    public double getAccountBalance() {
        return accountBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }
}
