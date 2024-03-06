package com.ringme.cms.controller.sys;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class LanguageController {

    @PostMapping("/change-language")
    public String changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request) {
        LocaleContextHolder.setLocale(new Locale(lang));
        return "redirect:/";
    }


}
