package com.inforsecure.fiuapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Access(AccessType.FIELD)
@JsonIgnoreProperties
@Table(name="consents_response")
public class ConsentResponses extends Audit{

    private static final long serialVersionUID = 3826179560700575385L;

    @Column(name="user_id")
    private String userId;

    @Column(name="consent_id")
    private String consentId;

    @Column(name="consent_hash")
    private String consentHash;

    @Column(name= "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name= "is_expired")
    private boolean isExpired;

}
