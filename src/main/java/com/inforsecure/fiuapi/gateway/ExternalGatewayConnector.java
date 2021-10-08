package com.inforsecure.fiuapi.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inforsecure.fiuapi.config.Properties;
import com.inforsecure.fiuapi.domain.Consent;
import com.inforsecure.fiuapi.domain.FI;
import com.inforsecure.fiuapi.domain.FIDataRange;
import com.inforsecure.fiuapi.gateway.assembler.ExternalGatewayAssembler;
import com.inforsecure.fiuapi.gateway.resource.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Component
@RequiredArgsConstructor
public class ExternalGatewayConnector {

    @Autowired
    private Properties properties;

    private final RestTemplate restTemplate;

    @Autowired
    private ExternalGatewayAssembler externalGatewayAssembler;

    public ExternalAnumatiResource post(Consent consent){
        String url = properties.getApi().getAaSetuUrl()+"Consent";

        ExternalGatewayRequestResource externalGatewayResource = externalGatewayAssembler.toGatewayResource(consent);

        ResponseEntity<GatewayConsentResponse>  responseEntity = postConsent(url,externalGatewayResource);

        GatewayConsentResponse gatewayConsentResponse = responseEntity.getBody();
        ExternalAnumatiResource externalAnumatiResource = null;
        if(gatewayConsentResponse!=null){
            externalAnumatiResource = externalGatewayAssembler.toAnumatiResource(gatewayConsentResponse);
        }
        // Can Save in DB
        return externalAnumatiResource;
    }

    public ExternalConsentStatusResource get(String ConsentHandle){
        String url = properties.getApi().getAaSetuUrl()+"Consent/handle/"+ConsentHandle;
        ResponseEntity<ExternalConsentStatusResource>  responseEntity = getConsentStatus(url,ConsentHandle);
        ExternalConsentStatusResource externalConsentStatusResource = null;
        if(responseEntity.getBody()!=null){
            externalConsentStatusResource = getConsentStatus(url,ConsentHandle).getBody();
        }
        return externalConsentStatusResource;
    }


    public ExternalSignedConsentResource fetchSignedConsent(String consentId){
        String url = properties.getApi().getAaSetuUrl()+"Consent/"+consentId;
        ResponseEntity<ExternalSignedConsentResource> responseEntity = getSignedConsent(url,"Consent/"+consentId);
        ExternalSignedConsentResource externalSignedConsentResource = null;
        if( responseEntity.getBody() !=null ){
            externalSignedConsentResource = responseEntity.getBody();
        }
        return externalSignedConsentResource;
    }

    public GenerateKeyMaterialResponseResource generateKeyMaterial(){
        String url = properties.getApi().getRahasyaUrl()+"ecc/v1/generateKey";

        ResponseEntity<GenerateKeyMaterialResponseResource> responseEntity =  restTemplate.getForEntity(
                url,GenerateKeyMaterialResponseResource.class
        );
        GenerateKeyMaterialResponseResource generateKeyMaterialResponseResource = null;
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            generateKeyMaterialResponseResource = responseEntity.getBody();
        }
        return generateKeyMaterialResponseResource;
    }

    public RequestFIDataResponseResource requestFIData(ExternalSignedConsentResource signedConsentResource,
                                                       GenerateKeyMaterialResponseResource generateKeyMaterial,
                                                       FIDataRange fiDataRange){
        String url = properties.getApi().getAaSetuUrl()+"/FI/request";
        RequestFIDataResponseResource FIDataResponse = null;

        RequestFIDataRequestResource FIDataRequestResource = externalGatewayAssembler.toFIDataRequestResource(
                signedConsentResource,generateKeyMaterial,fiDataRange
        );

        ResponseEntity<RequestFIDataResponseResource> responseEntity = postRequestFIData(url,FIDataRequestResource);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            FIDataResponse = responseEntity.getBody();
        }
        return FIDataResponse;
    }

    public FetchFIDataResponse getFIData(String sessionId){
        String url  = properties.getApi().getAaSetuUrl()+"/FI/fetch/"+sessionId;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client_api_key",properties.getCredintials().getClientApiKey());
        httpHeaders.add("x-jws-signature",JWTRequestSignature("/FI/fetch/"+sessionId));
        final HttpEntity<Object> entity = new HttpEntity<>(
                httpHeaders
        );

        ResponseEntity<FetchFIDataResponse> responseResponseEntity = restTemplate.exchange(
                url, HttpMethod.GET,entity,FetchFIDataResponse.class
        );

        if(responseResponseEntity.getStatusCode().equals(HttpStatus.OK)){
            return responseResponseEntity.getBody();
        }
        return null;
    }

    public DecryptFIDataResponse getDecryptedEncoder(GenerateKeyMaterialResponseResource keyMaterial,
                                                     FI fi, FI.FIData fiData){

        DecryptFIDataResource decryptFIDataResource = null;
        decryptFIDataResource = externalGatewayAssembler.toDecryptFIData(keyMaterial,fi,fiData);

        String url  = properties.getApi().getRahasyaUrl()+"/ecc/v1/decrypt";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client_api_key",properties.getCredintials().getClientApiKey());
        httpHeaders.add("x-jws-signature",JWTRequestSignature(decryptFIDataResource));
        final HttpEntity<Object> entity = new HttpEntity<>(
                decryptFIDataResource,
                httpHeaders
        );
        ResponseEntity<DecryptFIDataResponse> responseResponseEntity = restTemplate.exchange(
                url, HttpMethod.POST,entity,DecryptFIDataResponse.class
        );

        if(responseResponseEntity.getStatusCode().equals(HttpStatus.OK)){
            return responseResponseEntity.getBody();
        }
        return null;
    }

    public Object postToDataLearningApi(LinkedHashMap jsonObject){
        String url = properties.getApi().getDataLearningAPi()+"analyse";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        final HttpEntity<Object> entity = new HttpEntity<>(
                jsonObject,
                httpHeaders
        );
        ResponseEntity<Object> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST,entity,Object.class
        );
        if(responseEntity!=null){
            return responseEntity.getBody();
        }
        return null;
    }

    private ResponseEntity<GatewayConsentResponse> postConsent(String url, ExternalGatewayRequestResource externalGatewayResource){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client_api_key",properties.getCredintials().getClientApiKey());
        httpHeaders.add("x-jws-signature",JWTRequestSignature(externalGatewayResource));
        final HttpEntity<Object> entity = new HttpEntity<>(
                externalGatewayResource,
                httpHeaders
        );

        return restTemplate.exchange(
                url, HttpMethod.POST,entity,GatewayConsentResponse.class
        );
    }

    private ResponseEntity<ExternalConsentStatusResource> getConsentStatus(String url,String ConsentHandle){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client_api_key",properties.getCredintials().getClientApiKey());
        httpHeaders.add("x-jws-signature",JWTRequestSignature(ConsentHandle));
        final HttpEntity<Object> entity = new HttpEntity<>(
                httpHeaders
        );
        return restTemplate.exchange(
                url, HttpMethod.GET,entity,ExternalConsentStatusResource.class
        );
    }

    private ResponseEntity<ExternalSignedConsentResource> getSignedConsent(String url,String consentId){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client_api_key",properties.getCredintials().getClientApiKey());
        httpHeaders.add("x-jws-signature",JWTRequestSignature(consentId));
        final HttpEntity<Object> entity = new HttpEntity<>(
                httpHeaders
        );
        return restTemplate.exchange(
                url, HttpMethod.GET,entity,ExternalSignedConsentResource.class
        );
    }

    private ResponseEntity<RequestFIDataResponseResource> postRequestFIData(
                                            String url, RequestFIDataRequestResource FIDataRequestResource){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client_api_key",properties.getCredintials().getClientApiKey());
        httpHeaders.add("x-jws-signature",JWTRequestSignature(FIDataRequestResource));
        final HttpEntity<Object> entity = new HttpEntity<>(
                FIDataRequestResource,
                httpHeaders
        );
        return restTemplate.exchange(
                url, HttpMethod.POST,entity,RequestFIDataResponseResource.class
        );
    }

    private String JWTRequestSignature(Object object){

        try {
            String privateKey = properties.getCredintials().getSigningPrivateKey();
            String privateKeyPEM = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
            byte[] encoded = Base64.decodeBase64(privateKeyPEM);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            RSAPrivateKey privateRSAKey = (RSAPrivateKey) kf.generatePrivate(keySpec);
            JWSSigner signer = new RSASSASigner(privateRSAKey);
            JWSObject jwsObject = new JWSObject(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .type(JOSEObjectType.JWT)
                            .build(),
                    new Payload( new ObjectMapper().writeValueAsString(object))
             );
            jwsObject.sign(signer);
            String serializedJws = jwsObject.serialize();
            return createDetachedJws(serializedJws);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
     private String createDetachedJws(String Jws){
         String[] splittedJWS = Jws.split("\\.");
         splittedJWS[1] = "";
         return String.join(".", splittedJWS);
     }

}
