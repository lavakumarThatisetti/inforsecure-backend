package com.inforsecure.fiuapi.gateway.resource;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.inforsecure.fiuapi.domain.Customer;
import lombok.Data;

@Data
public class GatewayConsentResponse {

    @JsonProperty("ConsentHandle")
    public String ConsentHandle;
    @JsonProperty("Customer")
    public Customer Customer;
    public String timestamp;
    public String txnid;
    public String ver;
}
