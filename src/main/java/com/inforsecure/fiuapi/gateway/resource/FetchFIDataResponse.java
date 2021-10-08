package com.inforsecure.fiuapi.gateway.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.inforsecure.fiuapi.domain.FI;
import lombok.Data;

import java.util.ArrayList;


@Data
public class FetchFIDataResponse {
    @JsonProperty("FI")
    public ArrayList<FI> FI;
    public String timestamp;
    public String txnid;
    public String ver;
}
