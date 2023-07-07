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
package io.fusion.air.microservice.domain.entities.core.springdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@MappedSuperclass
public class AbstractBaseEntityWithUUID extends AbstractBaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "uuid", columnDefinition = "char(36)", unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    // Use the Below type if you are using PostgreSQL Database Exclusively
    // And Change the table script UUID data type to uuid
    // @Column(name = "uuid", columnDefinition = "uuid", unique = true)
    // @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID uuid;
    // @Size(min = 36, max = 36, message = "The length of ID Name must be 36 characters.")
    // @Pattern(regexp = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$", message = "Invalid UUID")


    /**
     * Returns the UUID
     * @return
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Returns UUID as a String
     * @return
     */
    @JsonIgnore
    public String getIdAsString() {
        return uuid.toString();
    }

    /**
     * Returns the Updated Time as A String Value
     * @return
     */
    public String getTime() {
        return super.getUpdatedTime().toString();
    }

    /**
     *
     * @return
     */
    public String toString() {
        return uuid != null ? uuid.toString() : "UUID Not Initialized!";
    }
}
