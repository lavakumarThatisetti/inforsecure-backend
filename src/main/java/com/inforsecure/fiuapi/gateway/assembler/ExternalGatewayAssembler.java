package com.inforsecure.fiuapi.gateway.assembler;

import com.inforsecure.fiuapi.config.Properties;
import com.inforsecure.fiuapi.domain.Consent;
import com.inforsecure.fiuapi.domain.FI;
import com.inforsecure.fiuapi.domain.FIDataRange;
import com.inforsecure.fiuapi.gateway.resource.*;
import com.inforsecure.fiuapi.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExternalGatewayAssembler {

    @Autowired
    private Properties properties;

    public ExternalGatewayRequestResource toGatewayResource(Consent consent){
        ExternalGatewayRequestResource externalGatewayResource = new ExternalGatewayRequestResource();

        externalGatewayResource.setVer("1.0");
        externalGatewayResource.setTxnid(UUID.randomUUID().toString());
        externalGatewayResource.setTimestamp(DateUtil.getCurrentTimeStamp());
        externalGatewayResource.setConsentDetail(consent);

        return externalGatewayResource;
    }
    public ExternalAnumatiResource toAnumatiResource(GatewayConsentResponse gatewayConsentResponse){
        ExternalAnumatiResource externalAnumatiResource = new ExternalAnumatiResource();
        String str = properties.getApi().getAnumatiUrl() +
                gatewayConsentResponse.getConsentHandle() +
                "?redirect_url=" + properties.getApi().getRedirectUrl()+"fetchData";
        externalAnumatiResource.setUrl(str);
        externalAnumatiResource.setDuplicate(false);
        return externalAnumatiResource;
    }

    public RequestFIDataRequestResource toFIDataRequestResource(ExternalSignedConsentResource signedConsentResource,
                                                                GenerateKeyMaterialResponseResource generateKeyMaterial,
                                                                FIDataRange fiDataRange){
        RequestFIDataRequestResource requestFIDataRequestResource = new RequestFIDataRequestResource();

        requestFIDataRequestResource.setVer(signedConsentResource.getVer());
        requestFIDataRequestResource.setTimestamp(DateUtil.getCurrentTimeStamp());
        requestFIDataRequestResource.setTxnid(UUID.randomUUID().toString());
        requestFIDataRequestResource.setFIDataRange(fiDataRange);
        requestFIDataRequestResource.setConsent(toConsent(signedConsentResource.getConsentId(),
                signedConsentResource.getSignedConsent().split("\\.")[2]));
        requestFIDataRequestResource.setKeyMaterial(generateKeyMaterial.getKeyMaterial());
        return requestFIDataRequestResource;
    }


    private RequestFIDataRequestResource.Consent toConsent(String consentID,String signedConsent){
        RequestFIDataRequestResource.Consent consent = new RequestFIDataRequestResource.Consent();
        consent.setId(consentID);
        consent.setDigitalSignature(signedConsent);
        return consent;
    }

    public DecryptFIDataResource toDecryptFIData(GenerateKeyMaterialResponseResource keyMaterial,
                                                  FI fiResponse,FI.FIData fiData){
        DecryptFIDataResource decryptFIDataResource = new DecryptFIDataResource();

        decryptFIDataResource.setBase64Data(fiData.getEncryptedFI());
        decryptFIDataResource.setBase64RemoteNonce(fiResponse.getKeyMaterial().getNonce());
        decryptFIDataResource.setBase64YourNonce(keyMaterial.getKeyMaterial().getNonce());
        decryptFIDataResource.setOurPrivateKey(keyMaterial.getPrivateKey());
        decryptFIDataResource.setRemoteKeyMaterial(fiResponse.getKeyMaterial());
        return decryptFIDataResource;
    }
}
