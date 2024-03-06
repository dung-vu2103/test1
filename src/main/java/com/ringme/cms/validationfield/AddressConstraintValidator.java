package com.ringme.cms.validationfield;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AddressConstraintValidator implements ConstraintValidator<Address, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.toLowerCase().matches("^[^~!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\\\/?]{3,25}$") && s.matches("\\D+");
        // s.toLowerCase().matches("^([a-vxyỳọáầảấờễàạằệếýộậốũứĩõúữịỗìềểẩớặòùồợãụủíỹắẫựỉỏừỷởóéửỵẳẹèẽổẵẻỡơôưăêâđ]+)((\\s{1}[a-vxyỳọáầảấờễàạằệếýộậốũứĩõúữịỗìềểẩớặòùồợãụủíỹắẫựỉỏừỷởóéửỵẳẹèẽổẵẻỡơôưăêâđ]+).{1,25})$")&&s.matches("\\D+");
    }
}
