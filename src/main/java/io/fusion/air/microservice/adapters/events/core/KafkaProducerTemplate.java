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
package io.fusion.air.microservice.adapters.events.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaProducerTemplate {

    @Autowired
    @Qualifier("kafkaProducerAcksByAll")
    private KafkaProducerAcksByAll kafkaProducerAcksByAll;
    @Autowired
    @Qualifier("kafkaProducerAcksByLeader")
    private KafkaProducerAcksByLeader kafkaProducerAcksByLeader;

    @Autowired
    @Qualifier("kafkaProducerAcksByNone")
    private KafkaProducerAcksByNone kafkaProducerAcksByNone;

    public KafkaProducerService getKafkaTemplate(String _ackType) {
        switch(_ackType) {
            case "all":
                return kafkaProducerAcksByAll;
            case "0":
                return kafkaProducerAcksByNone;
            default:
                return kafkaProducerAcksByLeader;
        }
    }

}
