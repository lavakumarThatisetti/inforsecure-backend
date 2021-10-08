package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inforsecure.fiuapi.domain.Consent;
import lombok.Data;

@Data
public class ExternalGatewayRequestResource {
    String ver;
    String timestamp;
    String txnid;
    @JsonProperty("ConsentDetail")
    Consent ConsentDetail;
}
