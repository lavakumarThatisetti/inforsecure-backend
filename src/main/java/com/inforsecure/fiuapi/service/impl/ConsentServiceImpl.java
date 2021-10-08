package com.inforsecure.fiuapi.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inforsecure.fiuapi.domain.*;
import com.inforsecure.fiuapi.gateway.ExternalGatewayConnector;
import com.inforsecure.fiuapi.gateway.resource.*;
import com.inforsecure.fiuapi.repository.ConsentRequestsRepository;
import com.inforsecure.fiuapi.repository.ConsentResponseRepository;
import com.inforsecure.fiuapi.repository.FiResponseRepository;
import com.inforsecure.fiuapi.repository.UserRepository;
import com.inforsecure.fiuapi.service.ConsentService;
import com.inforsecure.fiuapi.web.rest.assembler.ConsentAssembler;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class ConsentServiceImpl implements ConsentService {


    final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ExternalGatewayConnector externalGatewayConnector;

    @Autowired
    private ConsentAssembler consentAssembler;

    @Autowired
    private ConsentResponseRepository consentResponseRepository;

    @Autowired
    private ConsentRequestsRepository consentRequestsRepository;

    @Autowired
    private FiResponseRepository fiResponseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ExternalAnumatiResource createConsent(Consent consent, String userId) {

        saveRequestConsent(consent,userId);

        ExternalAnumatiResource externalAnumatiResource = externalGatewayConnector.post(consent);
        String hashcode = consentAssembler.toUniqueHashConsent(consent,userId);

        ConsentResponses consentResponses = consentAssembler.toConsentResponses(
                hashcode,
                externalAnumatiResource.getUrl(),
                consent.getConsentExpiry(),
                userId
        );
        consentResponseRepository.save(consentResponses);

        return externalAnumatiResource;
    }

    private void saveRequestConsent(Consent consent,String userId){

        ConsentRequests consentRequests = new ConsentRequests();
        consentRequests.setUserId(userId);
        try {
            consentRequests.setData(new ObjectMapper().writeValueAsString(consent));
            consentRequestsRepository.save(consentRequests);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public ArrayList<Object> getConsentStatus(String ConsentHandle, FIDataRange fiDataRange,String userId) {
        ExternalConsentStatusResource externalConsentStatusResource = externalGatewayConnector.get(ConsentHandle);
        ExternalSignedConsentResource externalSignedConsentResource =null;
        RequestFIDataResponseResource requestFIDataResponseResource = null;
        // Fetching Signed Consent
        if(externalConsentStatusResource.getConsentStatus().getStatus().equals("READY")){
            String consentId = externalConsentStatusResource.getConsentStatus().getId();
            externalSignedConsentResource  = externalGatewayConnector.fetchSignedConsent(consentId);
        }
        GenerateKeyMaterialResponseResource keyMaterial =null;
        SingedConsentAccounts singedConsentAccounts;
        // Generate Key material Resource
        if(externalSignedConsentResource!=null){
            singedConsentAccounts = ( SingedConsentAccounts) decryptBase64Data(
                    externalSignedConsentResource.getSignedConsent().split("\\.")[1],SingedConsentAccounts.class
            );
            assert singedConsentAccounts!=null;
            keyMaterial = externalGatewayConnector.generateKeyMaterial();
            requestFIDataResponseResource = externalGatewayConnector.requestFIData(
                    externalSignedConsentResource,keyMaterial, fiDataRange
            );
        }
        // Fetch FI Data
        FetchFIDataResponse fetchFIDataResponse = null;
        if(requestFIDataResponseResource !=null){
            fetchFIDataResponse = externalGatewayConnector.getFIData(requestFIDataResponseResource.getSessionId());
        }

        // Decrypt Fetch Data
        DecryptFIDataResponse decryptFIDataResponse;
        ArrayList<Object> objectArrayList = new ArrayList<>();
        int aggregatedWealthScore  = 0;
        if(fetchFIDataResponse !=null){
            for(FI fi : fetchFIDataResponse.getFI()){
                for(int i=0;i<fi.getData().size();i++){
                    decryptFIDataResponse = externalGatewayConnector.getDecryptedEncoder(keyMaterial,fi,fi.getData().get(i));
                    JSONObject object = (JSONObject) decryptBase64Data(decryptFIDataResponse.getBase64Data(), JSONObject.class);
                    assert object != null;
                    LinkedHashMap jsonObject = (LinkedHashMap) object.get("account");
                    // Need to send data to data learning api
                    Object json = externalGatewayConnector.postToDataLearningApi(jsonObject);
                    aggregatedWealthScore += (Integer) ((LinkedHashMap) json).get("wealthScore");

                    objectArrayList.add(json);
                }
            }
        }
        aggregatedWealthScore = aggregatedWealthScore/objectArrayList.size();

        FiDataResponses fiDataResponses =  consentAssembler.toFiDatResponse(userId,ConsentHandle,fiDataRange);
        fiDataResponses.setFiHash(consentAssembler.toUniqueHashFiData(userId,ConsentHandle,fiDataRange));
        fiDataResponses.setWealthScore(aggregatedWealthScore);
        try {
            fiDataResponses.setData(new ObjectMapper().writeValueAsString(objectArrayList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        fiResponseRepository.save(fiDataResponses);

        // Store wealth Score in user Repo;
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setWealthScore((user.getWealthScore()+aggregatedWealthScore)/2);
            userRepository.save(user);
        }
        return objectArrayList;
    }

    private Object decryptBase64Data(String encodeData, Class c){
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodeData);
            String decodedString = new String(decodedBytes);
            return  objectMapper.readValue(decodedString,c);
        }catch (Exception e){
            System.out.println("Jackson Decryption Error");
        }
    return null;
    }
}
