package com.inforsecure.fiuapi.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

import java.util.ArrayList;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.NONE)
@EqualsAndHashCode
public class UniqueHashConsent {
    public String userId;
    public String consentMode;
    public String fetchType;
    public ArrayList<String> consentTypes;
    public ArrayList<String> fiTypes;
    public Consent.DataConsumer DataConsumer;
    public Customer Customer;
    public Consent.Purpose Purpose;
    public FIDataRange FIDataRange;
    public Consent.DataLife DataLife;
    public Consent.Frequency Frequency;
    public ArrayList<Consent.DataFilter> DataFilter;
}
