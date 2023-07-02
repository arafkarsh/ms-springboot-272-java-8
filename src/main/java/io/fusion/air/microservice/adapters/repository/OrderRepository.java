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
package io.fusion.air.microservice.adapters.repository;

import io.fusion.air.microservice.domain.entities.example.OrderEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, UUID> {


    /**
     * Find Cart by Customer ID
     *
     * @param customerId
     * @return
     */
    public List<OrderEntity> findByCustomerId(String customerId);

    /**
     * Find Order By Customer ID
     *
     * @param _orderid
     * @param _customerId
     * @return
     */
    public Optional<OrderEntity> findByuuidAndCustomerId(UUID _orderid, String _customerId);

}
