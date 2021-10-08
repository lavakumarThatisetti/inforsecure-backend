package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inforsecure.fiuapi.domain.FIDataRange;
import lombok.Data;

@Data
public class RequestFIDataRequestResource {
    public String timestamp;
    public String txnid;
    public String ver;
    @JsonProperty("FIDataRange")
    public FIDataRange FIDataRange;
    @JsonProperty("Consent")
    public Consent Consent;
    @JsonProperty("KeyMaterial")
    public KeyMaterialResource KeyMaterial;

    @Data
    public static class Consent {
        String id;
        String digitalSignature;
    }
}
