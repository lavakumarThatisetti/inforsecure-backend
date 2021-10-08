package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingedConsentAccounts {

    @JsonProperty("Accounts")
    public ArrayList<Accounts> Accounts;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Accounts{
        public String fiType;
        public String fipId;
    }
}
