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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;


/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class ProductSpecification {

    /**
     * Search Product by Product Name
     * @param _productName
     * @return
     */
    public static Specification<ProductEntity> hasProductName(String _productName){
        return (product, cq, cb) -> cb.equal(product.get("productName"), _productName);
    }

    /**
     * Search Product By Product Price
     * @param _price
     * @return
     */
    public static Specification<ProductEntity> hasProductPrice(BigDecimal _price){
        return (product, cq, cb) -> cb.equal(product.get("productPrice"), _price);
    }

    /**
     * Search Product By Product Location
     * @param _zipCode
     * @return
     */
    public static Specification<ProductEntity> hasZipCode(String _zipCode){
        return (product, cq, cb) -> cb.equal(product.get("productLocationZipCode"), _zipCode);
    }

    /**
     * Select Product
     *
     * 1. By Product Name and
     * 2. By Location and
     * 3. By Price Greater than the Input Price
     *
     * @param _productName
     * @param _location
     * @param _price
     * @return
     */
    public static Specification<ProductEntity> hasProductAndLocationAndPriceGreaterThan(
            String _productName,  String _location, BigDecimal _price){
        return (Specification<ProductEntity>) (product, cq, cb) -> {
            // 1st Predicate
            Predicate equalPredicate = cb.and(
                    cb.equal(product.get("productName"), _productName),
                    cb.equal(product.get("productLocationZipCode"), _location)
            );
            // 2nd Predicate
            Predicate greaterThanPredicate = cb.greaterThan(product.get("productPrice"), _price);
            // Criteria Query - Order By
            cq.orderBy(cb.desc(product.get("productDetails")));
            // Return combined Predicate
            return cb.and(equalPredicate, greaterThanPredicate);
        };
    }
}
