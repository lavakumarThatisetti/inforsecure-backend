package com.inforsecure.fiuapi.validation;


import org.apache.commons.lang3.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumCheckValidator implements ConstraintValidator<EnumCheck, String> {
    private Class enumClass;

    @Override
    public void initialize(EnumCheck enumCheck) {
        this.enumClass = enumCheck.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       if(value == null){
           return true;
       }
       String validValues = Arrays.toString(enumClass.getEnumConstants());
       String message = "Invalid input, valid values are "+validValues;
       context.disableDefaultConstraintViolation();
       context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
       return !isEnumEnteredIsIndex(value) && EnumUtils.isValidEnum(enumClass,value);
    }

    private static boolean isEnumEnteredIsIndex(String enumValue){
        return enumValue.matches("-?\\d+(\\.\\d+)?");
    }
}
