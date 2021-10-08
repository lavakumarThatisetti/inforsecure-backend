package com.inforsecure.fiuapi.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Access(AccessType.FIELD)
@JsonIgnoreProperties
@Table(name="consent_requests")
public class ConsentRequests extends Audit {
    @Column(name="user_id")
    private String userId;
    @Column(name="data")
    private String data;
}
