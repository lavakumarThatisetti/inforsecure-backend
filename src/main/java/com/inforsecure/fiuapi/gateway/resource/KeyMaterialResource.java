package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KeyMaterialResource {
    public String cryptoAlg;
    public String curve;
    public String params;
    @JsonProperty("DHPublicKey")
    public DHPublicKey DHPublicKey;

    @Data
    public static class DHPublicKey{
        public String expiry;
        @JsonProperty("Parameters")
        public String Parameters;
        @JsonProperty("KeyValue")
        public String KeyValue;
    }
    @JsonProperty("Nonce")
    public String Nonce;
}
