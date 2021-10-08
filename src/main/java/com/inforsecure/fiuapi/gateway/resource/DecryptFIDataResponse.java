package com.inforsecure.fiuapi.gateway.resource;

import lombok.Data;

@Data
public class DecryptFIDataResponse {
    public String base64Data;
    public String errorInfo;
}
