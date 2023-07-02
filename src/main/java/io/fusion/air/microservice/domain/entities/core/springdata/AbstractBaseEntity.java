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
public abstract class AbstractBaseEntity {

    @JsonIgnore
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @JsonIgnore
    @LastModifiedBy
    @Column(nullable = false)
    private String updatedBy;

    @JsonIgnore
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @Column(name = "isActive")
    private boolean isActive;

    @Version
    @Column(name = "version")
    private int version;

    @PrePersist()
    public void initAudit() {
        this.isActive = true;
    }

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
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * Returns Last Modified By User
     * @return
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Returns Last Modified Date
     * @return
     */
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
}
