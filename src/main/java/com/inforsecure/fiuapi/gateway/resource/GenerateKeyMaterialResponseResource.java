package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GenerateKeyMaterialResponseResource {

    public String privateKey;
    public String errorInfo;
    @JsonProperty("KeyMaterial")
    public KeyMaterialResource KeyMaterial;
}
