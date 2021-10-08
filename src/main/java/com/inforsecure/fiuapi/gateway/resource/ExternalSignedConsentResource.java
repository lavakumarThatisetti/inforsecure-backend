package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalSignedConsentResource {
    @JsonProperty("ConsentUse")
    public ConsentUse ConsentUse;
    public String consentId;
    public String createTimestamp;
    public String signedConsent;
    public String status;
    public String timestamp;
    public String txnid;
    public String ver;

    @Data
    public static class ConsentUse{
        int count;
        String lastUseDateTime;
        String logUri;
    }
}
