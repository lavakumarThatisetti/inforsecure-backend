package com.inforsecure.fiuapi.web.rest.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inforsecure.fiuapi.domain.*;
import com.inforsecure.fiuapi.validation.ArrayEnumCheck;
import com.inforsecure.fiuapi.validation.EnumCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class ConsentResource {

    @NotNull
    @NotBlank
    public String userId;
    @EnumCheck(enumClass = ConsentMode.class)
    public String consentMode;
    @EnumCheck(enumClass = FetchType.class)
    public String fetchType;
    @ArrayEnumCheck(enumClass = ConsentType.class)
    public ArrayList<String> consentTypes;
    @ArrayEnumCheck(enumClass = FiType.class)
    public ArrayList<String> fiTypes;
    @JsonProperty("Purpose")
    public Purpose purpose;
    @JsonProperty("FIDataRange")
    public FIDataRange FIDataRange;
    @JsonProperty("DataLife")
    public DataLife DataLife;
    @JsonProperty("Frequency")
    public Frequency Frequency;
    @JsonProperty("DataFilter")
    public ArrayList<DataFilter> DataFilter;
    public String phoneNumber;

    @Data
    public static class Purpose {
        String code;
        String refUri;
        String text;
        @JsonProperty("Category")
        public Category Category;

        @Data
        public static class Category {
            String type;
        }
    }
    @Data
    public static class FIDataRange {
        String from;
        String to;
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
    public static class DataFilter {
        String type;
        String operator;
        String value;
    }
}
