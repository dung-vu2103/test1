package com.ringme.cms.validationfield;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameConstraintValidator implements ConstraintValidator<Name, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null) {
            s = s.trim().replaceAll("\s+", " ");
            if (s.equals(" ")) {
                return false;
            }
        }
        return s.toLowerCase().matches("^[^~!@#$%^&*()+\\=\\[\\]{};':\"\\\\|,.<>\\\\/?]{1,50}$");
        // s.toLowerCase().matches("^([a-vxyỳọáầảấờễàạằệếýộậốũứĩõúữịỗìềểẩớặòùồợãụủíỹắẫựỉỏừỷởóéửỵẳẹèẽổẵẻỡơôưăêâđ]+)((\\s{1}[a-vxyỳọáầảấờễàạằệếýộậốũứĩõúữịỗìềểẩớặòùồợãụủíỹắẫựỉỏừỷởóéửỵẳẹèẽổẵẻỡơôưăêâđ]+).{1,25})$")&&s.matches("\\D+");
//        return s.toLowerCase().matches("^[A-Za-z0-9-_\\p{L}\\p{M}]{1,40}+$")&& s.matches("\\D+");  // && s.matches("\\D+") là bỏ qua các s
    }
}
