package com.inforsecure.fiuapi.web.rest.controller;


import com.inforsecure.fiuapi.web.rest.resource.ConsentNotificationResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    @PostMapping(produces = "application/json",value = "/Consent/Notification")
    public ResponseEntity<?> createConsent(@RequestHeader HttpHeaders httpHeaders,
                                           @Valid @RequestBody ConsentNotificationResource consentNotificationResource) {

        System.out.println(consentNotificationResource.getNotifier().getType());
        return ResponseEntity.ok(consentNotificationResource);
    }
}
