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
@Table(name="fi_data_responses")
public class FiDataResponses  extends Audit{
    private static final long serialVersionUID = 3702683110099270781L;

    @Column(name="user_id")
    private String userId;

    @Column(name="consent_id")
    private String consentId;

    @Column(name= "from_date")
    private LocalDateTime fromDate;

    @Column(name= "to_date")
    private LocalDateTime toDate;

    @Column(name="fi_hash")
    private String fiHash;

    @Column(name= "data")
    private String data;

    @Column(name="wealth_score")
    private int wealthScore;
}
