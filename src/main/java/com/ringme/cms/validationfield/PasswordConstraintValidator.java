package com.ringme.cms.validationfield;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
//        return s.matches("^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[^a-zA-Z]).{8,40}$") && s.matches("\\S+"); // mk k có dấu cách
        return s.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])[A-Za-z\\d@#$%^&+=].{8,40}$"); //mk có dấu cách
    }
}
