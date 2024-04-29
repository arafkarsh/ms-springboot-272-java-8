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

/**
 * SOLID Principles
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
public interface BankAccountDetails {

    /**
     * Deposit Amount
     * @param _depositAmount
     */
    public void deposit(double _depositAmount) ;

    /**
     * Withdraw Amount
     * @param _withdrawalAmount
     */
    public void withdrawal(double _withdrawalAmount) ;

    /**
     * Calculate Interest based on Account Type
     * 1. Savings
     * 2. Checking
     * 3. Fixed
     * Calculate Interest
     */
    public double calculateInterest();
}
