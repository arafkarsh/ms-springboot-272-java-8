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
package io.fusion.air.microservice.domain.models.solid.d;

/**
 * SOLID Examples
 *
 * Dependency Inversion Principle (DIP):
 * High-level modules should not depend on low-level modules. Both should depend on
 * abstractions (e.g., interfaces), not on concretions (e.g., classes).
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
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class BankEmailNotificationService implements BankNotificationInterface {

    // This is pseudo code to explain the Single Responsibility Pattern
    public void sendNotification(String notification) {
        // Code to Send Notification
    }
}
