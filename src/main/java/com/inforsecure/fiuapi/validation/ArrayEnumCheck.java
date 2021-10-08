package com.inforsecure.fiuapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ArrayEnumCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArrayEnumCheck {
    String message() default "Value is not in the enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class enumClass();
}
