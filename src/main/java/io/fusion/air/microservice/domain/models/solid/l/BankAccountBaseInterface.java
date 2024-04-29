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

/**
 * SOLID Principles
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
public interface BankAccountBaseInterface {

    /**
     * Withdraw Amount
     * @param _withdrawalAmount
     */
    public void withdrawal(double _withdrawalAmount);

    /**
     * Calculate Interest based on Account Type
     * 1. Savings Account = 0.5%
     * 2. Checking Account = 0.1%
     * 3. Fixed Deposits = 0.7%
     * 4. SIP (Systematic Investment Plan) = 0.9%
     * Calculate Interest
     */
    public double calculateInterest();
}
