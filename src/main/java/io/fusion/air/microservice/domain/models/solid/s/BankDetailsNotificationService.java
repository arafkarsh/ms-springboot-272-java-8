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

/**
 * SOLID Examples
 *
 * Single Responsibility Principle (SRP)
 * A class should have only one reason to change, meaning it should have only one j
 * ob or responsibility.
 *
 * The refactored Code has 1 Responsibility
 *      1. Send Notification Details
 *
 * With Single Responsibility Pattern the Handling of Save Account Details
 * and sending Notification must be done in separate classes.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class BankDetailsNotificationService {

    // This is pseudo code to explain the Single Responsibility Pattern
    public void sendNotification(String notification) {
        // Code to Send Notification
    }
}
