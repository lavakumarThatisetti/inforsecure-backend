package com.inforsecure.fiuapi.web.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConsentNotificationResource {
    public String ver;
    public String timestamp;
    public String txnid;
    @JsonProperty("Notifier")
    public Notifier Notifier;
    @JsonProperty("ConsentStatusNotification")
    public ConsentStatusNotification ConsentStatusNotification;

    @Data
    public static class Notifier{
        String type;
        String id;
    }
    @Data
    public static class ConsentStatusNotification{
        String consentId;
        String consentHandle;
        String consentStatus;

    }
}
