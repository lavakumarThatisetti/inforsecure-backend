package com.inforsecure.fiuapi.gateway.resource;

import lombok.Data;

@Data
public class RequestFIDataResponseResource {
    public String consentId;
    public String sessionId;
    public String timestamp;
    public String txnid;
    public String ver;
}
