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
package io.fusion.air.microservice.adapters.service;

import io.fusion.air.microservice.adapters.repository.CountryGeoRepository;
import io.fusion.air.microservice.domain.entities.order.CountryEntity;
import io.fusion.air.microservice.adapters.repository.CountryRepository;
import io.fusion.air.microservice.domain.entities.order.CountryGeoEntity;
import io.fusion.air.microservice.domain.ports.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The @Transactional annotation is used in Spring to define the scope of a single logical transaction.
 * The database transactions happen within the boundaries of the method marked with this annotation.
 *
 * The annotation offers several options for customization:
 *
 * readOnly: This attribute is a hint for the persistence provider that the transaction will be read-only.
 * In our order, we use @Transactional(readOnly = true) because the method is just reading data from the
 * database and not modifying anything. This can allow the persistence provider and the database to apply
 * some optimizations.
 *
 * rollbackFor and noRollbackFor: These attributes allow you to define for which exceptions a rollback
 * should be performed or not.
 *
 * propagation: This attribute allows you to define how the transaction of the annotated method is related
 * to the surrounding transaction.
 *
 * isolation: This attribute allows you to define the isolation level for the transaction.
 *
 * timeout: This attribute allows you to define a timeout for the transaction.
 *
 * The @Transactional annotation in Spring is quite powerful and flexible. It's the basis for handling
 * transactions in a Spring application
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepositoryImpl;

    @Autowired
    private CountryGeoRepository countryGeoRepositoryImpl;

    /**
     * Get All Geo Countries
     * @return
     */
    @Transactional(readOnly = true)
    public Page<CountryGeoEntity> getAllGeoCountries() {
        return getAllGeoCountries(1, 10);
    }

    /**
     * Get All Geo Countries by Page Number and No. of Records (Size)
     *
     * @param page
     * @param size
     * @return
     */
    @Transactional(readOnly = true)
    public Page<CountryGeoEntity> getAllGeoCountries(int page, int size) {
        return countryGeoRepositoryImpl.findAll(PageRequest.of(page, size));
    }

    /**
     * Returns all the Countries
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<CountryEntity> getAllCountries() {
        return (List<CountryEntity>) countryRepositoryImpl.findAll();
    }

}
