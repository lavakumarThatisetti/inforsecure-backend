package com.inforsecure.fiuapi.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.NONE)
@EqualsAndHashCode
public class UniqueFiData {
    private static final long serialVersionUID = 669760252068671210L;

    @Column(name="user_id")
    private String userId;

    @Column(name="consent_id")
    private String consentId;

    @Column(name= "from_date")
    private LocalDateTime fromDate;

    @Column(name= "to_date")
    private LocalDateTime toDate;
}
