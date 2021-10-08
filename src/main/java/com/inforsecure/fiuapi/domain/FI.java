package com.inforsecure.fiuapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inforsecure.fiuapi.gateway.resource.FetchFIDataResponse;
import com.inforsecure.fiuapi.gateway.resource.KeyMaterialResource;
import lombok.Data;

import java.util.ArrayList;

@Data
public class FI {
    @JsonProperty("KeyMaterial")
    public KeyMaterialResource KeyMaterial;

    public ArrayList<FIData> data;

    public String fipId;

    @Data
    public static class FIData{
        public String encryptedFI;
        public String linkRefNumber;
        public String maskedAccNumber;
    }
}
