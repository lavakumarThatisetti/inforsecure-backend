package com.inforsecure.fiuapi.web.rest.assembler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inforsecure.fiuapi.domain.*;
import com.inforsecure.fiuapi.repository.ConsentResponseRepository;
import com.inforsecure.fiuapi.repository.FiResponseRepository;
import com.inforsecure.fiuapi.util.Constants;
import com.inforsecure.fiuapi.util.DateUtil;
import com.inforsecure.fiuapi.web.rest.resource.ConsentResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ConsentAssembler {

    @Autowired
    private ConsentResponseRepository consentResponseRepository;

    @Autowired
    private FiResponseRepository fiResponseRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


    public Consent toConsentObject(ConsentResource consentResource){
        Consent consent = new Consent();

        consent.setConsentStart(DateUtil.getCurrentTimeStamp());
        consent.setConsentExpiry(DateUtil.getPeriodicTimeStamp(1));
        consent.setConsentMode(consentResource.getConsentMode());
        consent.setFetchType(consentResource.getFetchType());
        consent.setConsentTypes(consentResource.getConsentTypes());
        consent.setFiTypes(consentResource.getFiTypes());
        consent.setDataConsumer(toDataConsumer(Constants.FIU));
        consent.setCustomer(toCustomer(consentResource.getPhoneNumber()));
        consent.setPurpose(toPurpose(consentResource.getPurpose()));
        consent.setFIDataRange(toFiDataRange(consentResource.getFIDataRange()));
        consent.setDataLife(toDataLife(consentResource.getDataLife()));
        consent.setFrequency(toFrequency(consentResource.getFrequency()));
        consent.setDataFilter(toDataFilter(consentResource.getDataFilter()));

        return consent;
    }

    private Consent.DataConsumer toDataConsumer(String dataConsumerResource){
        Consent.DataConsumer consumer = new Consent.DataConsumer();
        consumer.setId(dataConsumerResource);
        return consumer;
    }

    private Customer toCustomer(String phoneNumber){
        Customer customer = new Customer();
        customer.setId(phoneNumber+"@"+ Constants.AA);
        return customer;
    }

    private Consent.Purpose toPurpose(ConsentResource.Purpose purposeResource){
        Consent.Purpose purpose = new Consent.Purpose();
        purpose.setCode(purposeResource.getCode());
        purpose.setCategory(toCategory(purposeResource.getCategory()));
        purpose.setRefUri(purposeResource.getRefUri());
        purpose.setText(purposeResource.getText());
        return purpose;
    }
    private  Consent.Purpose.Category toCategory(ConsentResource.Purpose.Category categoryResource){
        Consent.Purpose.Category category = new Consent.Purpose.Category();
        category.setType(categoryResource.getType());
        return category;
    }
    private FIDataRange toFiDataRange(ConsentResource.FIDataRange fiDataResource){
        FIDataRange fiDataRange = new FIDataRange();
        fiDataRange.setFrom(fiDataResource.getFrom());
        fiDataRange.setTo(fiDataResource.getTo());
        return fiDataRange;
    }
    private Consent.DataLife toDataLife(ConsentResource.DataLife datalifeResource){
        Consent.DataLife dataLife = new Consent.DataLife();
        dataLife.setUnit(datalifeResource.getUnit());
        dataLife.setValue(datalifeResource.getValue());
        return dataLife;
    }
    private Consent.Frequency toFrequency(ConsentResource.Frequency frequencyResource){
        Consent.Frequency frequency = new Consent.Frequency();
        frequency.setUnit(frequencyResource.getUnit());
        frequency.setValue(frequencyResource.getValue());
        return frequency;
    }
    private ArrayList<Consent.DataFilter> toDataFilter(ArrayList<ConsentResource.DataFilter> dataFiltersResourceList){

        ArrayList<Consent.DataFilter> dataFilterList = new ArrayList<>();
        dataFiltersResourceList.forEach(dataFilter -> dataFilterList.add(new Consent.DataFilter(dataFilter.getType(),dataFilter.getOperator(),dataFilter.getValue())));
        return dataFilterList;
    }

    public String validateRequestHash(Consent consent,String userId){
        String hashCode = toUniqueHashConsent(consent,userId);

        Optional<ConsentResponses> OptionalConsentResponses = consentResponseRepository.findByConsentHash(hashCode);

        if(OptionalConsentResponses.isPresent()){
            ConsentResponses consentResponses = OptionalConsentResponses.get();
            if(consentResponses.getExpiryDate().isAfter(LocalDateTime.now())){
                return consentResponses.getConsentId();
            }else{
                return null;
            }
        }
        return null;
    }

    public String toUniqueHashConsent(Consent consent,String userId){
        StringBuilder str = new StringBuilder();
        str.append(userId)
                .append(consent.getConsentMode())
                .append(consent.getConsentTypes())
                .append(consent.getCustomer())
                .append(consent.getDataConsumer())
                .append(consent.getDataFilter())
                .append(consent.getDataLife())
                .append(consent.getFIDataRange())
                .append(consent.getFiTypes())
                .append(consent.getFrequency())
                .append(consent.getFetchType())
                .append(consent.getPurpose());
        String hashCode = new String((Base64.getEncoder().encode((str.toString()).getBytes())));

        return  hashCode;
    }

    public ConsentResponses toConsentResponses(String consentHash,
                                               String url,
                                               String expiryDate,
                                               String userId){
        return ConsentResponses.builder()
                .consentHash(consentHash)
                .consentId(url.split("/")[3].split("\\?")[0])
                .expiryDate(LocalDateTime.parse(expiryDate,formatter))
                .userId(userId)
                .isExpired(false)
                .build();
    }

    public String  toUniqueHashFiData(String userId, String consentId, FIDataRange fiDataRange){

        String hashCode = new String((Base64.getEncoder().encode((userId + consentId + fiDataRange.hashCode()).getBytes())));

        return hashCode;
    }

    public FiDataResponses toFiDatResponse(String userId, String consentId, FIDataRange fiDataRange){
        return FiDataResponses.builder()
                .userId(userId)
                .consentId(consentId)
                .fromDate(LocalDateTime.parse(fiDataRange.getFrom(),formatter))
                .toDate(LocalDateTime.parse(fiDataRange.getTo(),formatter))
                .build();
    }

    public Object getFiDataIfExists(String userId, String consentId, FIDataRange fiDataRange){
        String fiHashCode  = toUniqueHashFiData(userId, consentId, fiDataRange);
        Optional<FiDataResponses> optionalFiDataResponses = fiResponseRepository.findByFiHash(fiHashCode);

        if(optionalFiDataResponses.isPresent()){
            FiDataResponses fiDataResponses = optionalFiDataResponses.get();
            try {
                return new ObjectMapper().readValue(fiDataResponses.getData(),ArrayList.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
