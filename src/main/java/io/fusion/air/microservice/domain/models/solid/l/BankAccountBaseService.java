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
package io.fusion.air.microservice.domain.models.solid.l;

import io.fusion.air.microservice.domain.models.solid.s.BankDetailsNotificationService;
import io.fusion.air.microservice.domain.models.solid.s.BankDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SOLID Examples
 *
 * Liskov Substitution Principle (LSP):
 * Objects of a superclass should be replaceable with objects of its subclasses
 * without affecting the correctness of the program.
 *
 * This means all the sub-classes of BankAccount Interface must implement the
 * following interfaces
 * 1. deposit()
 * 2. withdrawal()
 * 3. calculateInterest()
 *
 * The issue with above 3 methods available on Interface.
 * - All the implementing classes must have an implementation for all the 3 methods.
 * - If a Sub Class doesn’t need the deposit() method then it may throw an Exception
 *   saying no such method error. Which can cause problems in being consistent and will
 *   affect the following definition.
 *
 * Objects of a superclass should be replaceable with objects of its subclasses
 * without affecting the correctness of the program.
 *
 * Model with Issues
 *
 *                                              |-------------------------------|
 *                                             |   BankAccountAllService         |
 *                                            |     - deposit()                      |
 *                                           |     - withdrawal()                 |
 *                                          |     - calculateInterest()         |
 *                                         |-------------------------------|
 *                                                           |
 *                 +-----------------------+-----------------+--------------------+
 *                 |                             |                       |                         |
 *           |----+--------------|    |----+----------|    |----+-------------|  |----+---------|
 *           |  Savings...Service  |    | Fixed...Service |   | Checking...Service |  | SIP...Service |
 *           |-------------------|    |---------------|   |------------------|   |--------------|
 *
 *   In the above accounts the recurring deposits happen only for the following types of accounts
 *   and allow multiple withdrawals (as well as deposits).
 *   1. Savings Account
 *   2. Checking Account
 *   3. SIP Account
 *
 *   While the FixedDeposit Account Type will have only multiple withdrawals after the account
 *   creation. So, when you invoke the deposit() method in Fixed Deposit it should throw an error.
 *   This is the actual issue which breaks the following rule:
 *   Objects of a superclass should be replaceable with objects of its subclasses
 *  without affecting the correctness of the program.
 *
 *  Solution
 *
 *             |---------------------------|
 *            |  BankAccountBaseService  |
 *           |     - withdrawal()           |
 *          |     - calculateInterest()    |
 *         |------------+--------------|
 *                       |
 *                      +---------------------------------------+
 *                      |                                                |
 *                     |                         |-----------------+-----------------|
 *                    |                         |  BankAccountBaseDepositService   |
 *                   |                        |     - deposit()                          |
 *                  |                        |-------------------+---------------|
 *                 |                                                 |
 *                |                                  +-----------+-----------+-------------------+
 *               |                                  |                              |                         |
 *           |--+--------------|        |----+------------|    |--------+---------|     |-------+-----|
 *          |  Fixed...Service  |        | Savings...Service |    | Checking...Service |     | SIP...Service |
 *         |-----------------|        |-----------------|    |-------------------|     |--------------|
 *
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public abstract class BankAccountBaseService implements BankAccountBaseInterface {

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
