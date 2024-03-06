package com.ringme.cms.validationfield;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullConstraintValidator implements ConstraintValidator<NotNullCus, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            if (!s.trim().equals("")) {
                return true;
            }
        }
        return false;
    }
}
