package com.ringme.cms.validationfield;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = AddressConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Address {
        //error message
        //public String message() default "*Không bao gồm các ký tự đặc biệt!";
        public String message() default "{validaitonAddress}";
        //represents group of constraint
        public Class<?>[] groups() default {};

        //represents additional information about annotation
        public Class<? extends Payload>[] payload() default {};
}
