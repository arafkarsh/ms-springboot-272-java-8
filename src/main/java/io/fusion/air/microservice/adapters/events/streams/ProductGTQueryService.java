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
package io.fusion.air.microservice.adapters.events.streams;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fusion.air.microservice.domain.exceptions.DataNotFoundException;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class ProductGTQueryService {

    @Autowired
    private  StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Query The GlobalKTable for UUID and return the Latest Product Info
     * @param uuid
     * @return
     * @throws JsonProcessingException
     */
    public JsonNode queryByUUID(String uuid) throws JsonProcessingException {
        String storeName = "products-store";

        // Query the store
        ReadOnlyKeyValueStore<String, String> keyValueStore = streamsBuilderFactoryBean
                        .getKafkaStreams()
                        .store(
                                StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore())
                        );
        // Retrieve the Product based on UUID from the KeyValueStore
        String result = keyValueStore.get(uuid);
        if(result != null) {
            System.out.println("GTQ>> Match Input UUID ("+uuid+") with Result UUID = ("+result+")");
            return objectMapper.readTree(result);
        }
        throw new DataNotFoundException("Data Not Found for Product UUID = "+uuid);
    }

    /**
     * WARNING: Querying ALL DATA is ONLY FOR DEMO PURPOSE
     * NOT TO BE USED In PRODUCTION SCENARIOS.
     * @return
     * @throws JsonProcessingException
     */
    public List<JsonNode> queryAllRecords() throws JsonProcessingException {
        String storeName = "products-store";
        List<JsonNode> allProducts = new ArrayList<>();

        // Query the store
        ReadOnlyKeyValueStore<String, String> keyValueStore = streamsBuilderFactoryBean
                .getKafkaStreams()
                .store(StoreQueryParameters
                        .fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
        // Fetch the values
        KeyValueIterator<String, String> range = keyValueStore.all();
        while (range.hasNext()) {
            KeyValue<String, String> next = range.next();
            JsonNode product = objectMapper.readTree(next.value);
            // System.out.println("GTQ >> Key = "+next.key+" Value = "+next.value);
            allProducts.add(product);
        }
        range.close();

        return allProducts;
    }

    /**
     public JsonNode queryByUUIDOld(String uuid) throws JsonProcessingException {
     String storeName = "products-store";

     // Query the store
     ReadOnlyKeyValueStore<String, String> keyValueStore = streamsBuilderFactoryBean
     .getKafkaStreams()
     .store(
     StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore())
     );
     String result = null;
     // This code to be fixed with Storing UUID as the key
     // And Avoid Full Table Scan
     KeyValueIterator<String, String> range = keyValueStore.all();
     while (range.hasNext()) {
     KeyValue<String, String> next = range.next();
     JsonNode product = objectMapper.readTree(next.value);
     System.out.println("GT >> Key = "+next.key+" Value = "+next.value);
     result = product.get("uuid").toString().replace("\"", "");
     // System.out.println("Match Input UUID ("+uuid+") with Result UUID = ("+result+")");
     if(result.equalsIgnoreCase(uuid)) {
     return product;
     }
     }
     throw new DataNotFoundException("Data Not Found for Product UUID = "+uuid);
     }
     */

}
