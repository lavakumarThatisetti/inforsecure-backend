package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalConsentStatusResource {

    @JsonProperty("ConsentHandle")
    public String ConsentHandle;
    @JsonProperty("ConsentStatus")
    public ConsentStatus ConsentStatus;
    public String timestamp;
    public String txnid;
    public String ver;

    @Data
    public static class ConsentStatus{
        String id;
        String status;
    }
}
