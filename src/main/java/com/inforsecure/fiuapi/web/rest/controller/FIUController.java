package com.inforsecure.fiuapi.web.rest.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inforsecure.fiuapi.domain.Consent;
import com.inforsecure.fiuapi.domain.ConsentRequests;
import com.inforsecure.fiuapi.domain.FIDataRange;
import com.inforsecure.fiuapi.domain.User;
import com.inforsecure.fiuapi.gateway.resource.ExternalAnumatiResource;
import com.inforsecure.fiuapi.repository.ConsentRequestsRepository;
import com.inforsecure.fiuapi.repository.UserRepository;
import com.inforsecure.fiuapi.service.ConsentService;
import com.inforsecure.fiuapi.web.rest.assembler.ConsentAssembler;
import com.inforsecure.fiuapi.web.rest.resource.ConsentResource;
import com.inforsecure.fiuapi.web.rest.resource.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping(path="/api/fiu")
@RequiredArgsConstructor
public class FIUController {

    @Autowired
    private ConsentAssembler consentAssembler;

    @Autowired
    private ConsentService consentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConsentRequestsRepository consentRequestsRepository;

    @PostMapping(produces = "application/json",value = "/createConsent")
    public ResponseEntity<?> createConsent(@Valid @RequestBody ConsentResource consentResource) {

        Consent consent =  consentAssembler.toConsentObject(consentResource);

        // Validate if request is already served based on hash request
        String consentId = consentAssembler.validateRequestHash(consent,consentResource.getUserId());

        if(consentId !=null){
            ExternalAnumatiResource externalAnumatiResource = new ExternalAnumatiResource();
            externalAnumatiResource.setUrl(consentId);
            externalAnumatiResource.setDuplicate(true);
            return ResponseEntity.ok(externalAnumatiResource);
        }

        ExternalAnumatiResource externalAnumatiResource = consentService.createConsent(consent,consentResource.getUserId());

        return ResponseEntity.ok(externalAnumatiResource);
    }

    @GetMapping(produces = "application/json",value = "/getConsents/{userId}")
    public ResponseEntity<?> getConsnetsOfUser(@RequestHeader HttpHeaders httpHeaders,
                                       @PathVariable String userId) {

        Optional<User> Ouser = userRepository.findById(UUID.fromString(userId));
        if(!Ouser.isPresent()){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is Not Presented!"));
        }

        List<ConsentRequests> consentRequests =  consentRequestsRepository.findTop30ConsentData(userId);
        List<Consent> allconsensts = new ArrayList<>();
        consentRequests.forEach(consents -> {
            try {
                allconsensts.add(new ObjectMapper().readValue(consents.getData(),Consent.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok(allconsensts);
    }



    @GetMapping(produces = "application/json",value = "/fiData/{userId}/{consentHandle}/{fromDate}/{toDate}")
    public ResponseEntity<?> getFiData(@RequestHeader HttpHeaders httpHeaders,
                                              @PathVariable String userId,
                                              @PathVariable String consentHandle,
                                              @PathVariable String fromDate,
                                              @PathVariable String toDate) {
        // Get Data within Date Ranges
        FIDataRange fiDataRange = new FIDataRange();
        fiDataRange.setFrom(fromDate);
        fiDataRange.setTo(toDate);

        Optional<User> Ouser = userRepository.findById(UUID.fromString(userId));
        if(!Ouser.isPresent()){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is Not Presented!"));
        }

        Object fiData = consentAssembler.getFiDataIfExists(userId,consentHandle,fiDataRange);

        if(fiData != null){
            return ResponseEntity.ok(fiData);
        }
        ArrayList<Object> externalConsentStatusResource = consentService.getConsentStatus(consentHandle,fiDataRange,userId);

        return ResponseEntity.ok(externalConsentStatusResource);
    }


}
