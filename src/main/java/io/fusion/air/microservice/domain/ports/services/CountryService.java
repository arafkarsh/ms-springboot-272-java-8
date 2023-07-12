/**
 * (C) Copyright 2021 Araf Karsh Hamid
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
package io.fusion.air.microservice.domain.ports.services;

import io.fusion.air.microservice.domain.entities.order.CountryEntity;
import io.fusion.air.microservice.domain.entities.order.CountryGeoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Country Service
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public interface CountryService {


    /**
     * Get All Geo Countries
     * @return
     */
    public Page<CountryGeoEntity> getAllGeoCountries();

    /**
     * Get All Geo Countries by Page Number and No. of Records (Size)
     * @param page
     * @param size
     * @return
     */
    public Page<CountryGeoEntity> getAllGeoCountries(int page, int size);

    /**
     * Returns all the Countries
     * @return
     */
    public List<CountryEntity> getAllCountries();
}
