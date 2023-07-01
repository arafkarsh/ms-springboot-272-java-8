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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractBaseEntity {

    @JsonIgnore
    @CreatedBy
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    private LocalDateTime creationDate;

    @JsonIgnore
    @LastModifiedBy
    private String lastModifiedBy;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @Column(name = "isActive")
    private boolean isActive;

    @Version
    @Column(name = "version")
    private int version;


    /**
     * De-Activate Record
     */
    @JsonIgnore
    public void deActivate() {
        isActive = false;
    }

    /**
     * Activate Record
     */
    @JsonIgnore
    public void activate() {
        isActive = true;
    }

    /**
     * Returns if the Record is Active
     * @return
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Returns the version of the Record
     * @return
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns The User Who created the record
     * @return
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Returns the Record Creation Date
     * @return
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns Last Modified By User
     * @return
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Returns Last Modified Date
     * @return
     */
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
}
