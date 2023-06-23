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

import java.math.BigDecimal;
import java.time.LocalDate;

import io.fusion.air.microservice.domain.entities.example.ProductEntity;
import org.springframework.data.jpa.domain.Specification;


/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class ProductSpecification {

    public static Specification<ProductEntity> hasProductName(String _productName){
        return (product, cq, cb) -> cb.equal(product.get("productName"), _productName);
    }

    public static Specification<ProductEntity> hasProductPrice(BigDecimal _pricec){
        return (product, cq, cb) -> cb.equal(product.get("productPrice"), _pricec);
    }

    public static Specification<ProductEntity> hasZipCode(String _zipCode){
        return (product, cq, cb) -> cb.equal(product.get("productLocationZipCode"), _zipCode);
    }
}
