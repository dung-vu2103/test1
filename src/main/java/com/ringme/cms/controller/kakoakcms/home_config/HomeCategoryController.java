package com.ringme.cms.controller.kakoakcms.home_config;

import com.ringme.cms.model.kakoakcms.home_config.HomeCategory;
import com.ringme.cms.service.kakoakcms.home_config.HomeCategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequestMapping("/home-category")
public class HomeCategoryController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    HomeCategoryService service;

    @GetMapping(value = {"/index"})
    public String getAllPage(Model model) {
        List<HomeCategory> list = service.get();
        model.addAttribute("models", list);
        model.addAttribute("max", list.size());
        model.addAttribute("title", messageSource.getMessage("title.homeCategory", null, LocaleContextHolder.getLocale()));
        return "home-category/index";
    }
    @PostMapping(value = {"/priority"})
    public String priority(@RequestParam("id") int id,
                           @RequestParam("priority-old") int priorityOld,
                           @RequestParam("priority-new") int priorityNew, Model model) {
        log.info("idd|{}|old|{}|new|{}", id, priorityOld, priorityNew);
        service.priority(id, priorityOld, priorityNew);
        return getAllPage(model);
    }
}
