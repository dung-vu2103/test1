package com.ringme.cms.validationfield;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class BirthDateConstraintValidator implements ConstraintValidator<BirthDate, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        LocalDate localDate = LocalDate.now();
        if (date == null) return false;
        else {
            try {
                Date date1 = Date.valueOf(date);
                return Period.between(date1.toLocalDate(), localDate).getYears() > 13;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
