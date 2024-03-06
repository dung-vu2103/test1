package com.ringme.cms.controller;

import com.ringme.cms.dto.kakoakcms.MinDto;
import com.ringme.cms.model.kakoakcms.Min;
import com.ringme.cms.service.MinService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.regex.PatternSyntaxException;

@Log4j2
@Controller
@RequestMapping("/admin11")
public class MinController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    MinService adminService;
    @GetMapping("/get")
    private String index1(@PathVariable(required = false) Integer page,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize,
                          @RequestParam(value = "name",required = false) String name
                          ,Model model){
        if(page == null){
            page=1;
        }
        if (pageSize == null){
            pageSize=10;
        }
        MinDto minDto=adminService.processSearch(name);
        Page<Min> oblectPage=adminService.get(minDto,page,pageSize);
        List<Min> admins=oblectPage.toList();
        log.info("listttt" +admins);
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", oblectPage.getTotalPages());
        model.addAttribute("totalItems", oblectPage.getTotalElements());
        model.addAttribute("admins", admins);
        model.addAttribute("title", messageSource.getMessage("title.min", null, LocaleContextHolder.getLocale()));
        return "index1";
    }
}
