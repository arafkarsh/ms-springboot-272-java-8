package io.fusion.air.microservice.adapters.events.core;

/**
 * Kafka Member DTO
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class KafkaMemberDTO {

    private String memberId;
    private String clientId;
    private String host;


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
