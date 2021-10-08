package com.inforsecure.fiuapi.service;

import com.inforsecure.fiuapi.domain.Consent;
import com.inforsecure.fiuapi.domain.FIDataRange;
import com.inforsecure.fiuapi.gateway.resource.ExternalAnumatiResource;
import com.inforsecure.fiuapi.gateway.resource.ExternalConsentStatusResource;
import com.inforsecure.fiuapi.gateway.resource.GatewayConsentResponse;

import java.util.ArrayList;

public interface ConsentService {
    ExternalAnumatiResource createConsent(Consent consent,String userId);
    ArrayList<Object> getConsentStatus(String ConsentHandle, FIDataRange fiDataRange,String userId);

}
