package com.inforsecure.fiuapi.domain;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.NONE)
public class Consent {
    public String consentStart;
    public String consentExpiry;
    public String consentMode;
    public String fetchType;
    public ArrayList<String> consentTypes;
    public ArrayList<String> fiTypes;
    public DataConsumer DataConsumer;
    public Customer Customer;
    public Purpose Purpose;
    public FIDataRange FIDataRange;
    public DataLife DataLife;
    public Frequency Frequency;
    public ArrayList<DataFilter> DataFilter;

    @Data
    public static class DataConsumer {
        String id;
    }

    @Data
    public static class Purpose {
        String code;
        String refUri;
        String text;
        @JsonProperty("Category")
        Category Category;

        @Data
        public static class Category {
            String type;
        }
    }
    @Data
    public static class DataLife {
        DataLifeUnit unit;
        int value;
    }

    @Data
    public static class Frequency {
        FrequencyUnit unit;
        int value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataFilter {
        String type;
        String operator;
        String value;
    }
}
