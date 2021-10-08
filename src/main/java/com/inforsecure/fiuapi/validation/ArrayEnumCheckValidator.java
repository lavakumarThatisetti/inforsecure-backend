package com.inforsecure.fiuapi.validation;


import org.apache.commons.lang3.EnumUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;

public class ArrayEnumCheckValidator implements ConstraintValidator<ArrayEnumCheck, ArrayList<String>> {
    private Class enumClass;

    @Override
    public void initialize(ArrayEnumCheck enumCheck) {
        this.enumClass = enumCheck.enumClass();
    }

    @Override
    public boolean isValid(ArrayList<String> values, ConstraintValidatorContext context) {
       if(values.size() == 0){
           return true;
       }
       String validValues = Arrays.toString(enumClass.getEnumConstants());
       String message = "Invalid input, valid values are "+validValues;
       context.disableDefaultConstraintViolation();
       context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
       return !isEnumEnteredIsIndex(values,enumClass);
    }

    private static boolean isEnumEnteredIsIndex(ArrayList<String> values,Class enumClass)
    {
        for(String value:values){
            if(!value.matches("-?\\d+(\\.\\d+)?") && EnumUtils.isValidEnum(enumClass,value)) return false;
        }
        return true;
    }
}
