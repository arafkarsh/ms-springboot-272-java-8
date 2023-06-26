package io.fusion.air.microservice.adapters.events.core;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Kafka Consumer Service
 * To See All the Consumers within a Consumer Group.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    /**
     * Returns all the consumers Across all the consumer groups
     * @param _topic
     * @return
     */
    public  Map<String, List<KafkaMemberDTO>> getConsumersAcrossGroups(String _topic) {
        List<String> groups = getConsumerGroupsForTopic(_topic);
        Map<String, List<MemberDescription>> consumers = getConsumerGroupsAndMembers(groups.toArray(new String[0]));
        Map<String, List<KafkaMemberDTO>> result = new HashMap<>();

        for (Map.Entry<String, List<MemberDescription>> entry : consumers.entrySet()) {
            List<KafkaMemberDTO> memberDtos = entry.getValue().stream().map(member -> {
                KafkaMemberDTO dto = new KafkaMemberDTO();
                dto.setMemberId(member.consumerId());
                dto.setClientId(member.clientId());
                dto.setHost(member.host());
                return dto;
            }).collect(Collectors.toList());

            result.put(entry.getKey(), memberDtos);
        }
        return result;
    }

    /**
     * Get the Consumer Groups
     *
     * @param topic
     * @return
     */
    public List<String> getConsumerGroupsForTopic(String topic)  {
        try (AdminClient admin = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {

            // Get list of all consumer groups
            List<ConsumerGroupListing> groupListings = admin.listConsumerGroups(new ListConsumerGroupsOptions())
                    .all().get().stream()
                    .collect(Collectors.toList());

            // Filter consumer groups consuming from the given topic
            List<String> groupsConsumingTopic = groupListings.stream()
                    .filter(groupListing -> {
                        try {
                            return admin.listConsumerGroupOffsets(
                                    groupListing.groupId(), new ListConsumerGroupOffsetsOptions())
                                    .partitionsToOffsetAndMetadata().get().keySet().stream()
                                    .anyMatch(topicPartition -> topicPartition.topic().equals(topic));
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException("Failed to get offsets from consumer group = ", e);
                        }
                    })
                    .map(ConsumerGroupListing::groupId)
                    .collect(Collectors.toList());

            return groupsConsumingTopic;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get consumers = ", e);
        }
    }

    /**
     * Get the Consumer Groups and its Members
     *
     * @param groupIds
     * @return
     */
    public Map<String, List<MemberDescription>> getConsumerGroupsAndMembers(String... groupIds) {
        try (AdminClient admin = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            Collection<String> groupIdList = Arrays.asList(groupIds);
            DescribeConsumerGroupsResult describeConsumerGroupsResult = admin.describeConsumerGroups(groupIdList);

            Map<String, KafkaFuture<ConsumerGroupDescription>> futures = describeConsumerGroupsResult.describedGroups();
            Map<String, List<MemberDescription>> groupMembersMap = new HashMap<>();

            for (Map.Entry<String, KafkaFuture<ConsumerGroupDescription>> entry : futures.entrySet()) {
                try {
                    List<MemberDescription> members = (List<MemberDescription>) entry.getValue().get().members();
                    groupMembersMap.put(entry.getKey(), members);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Failed to describe consumer group", e);
                }
            }
            return groupMembersMap;
        }
    }
}
