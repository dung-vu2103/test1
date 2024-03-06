package com.ringme.cms.controller.kakoakcms.notification;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.notification.NotiTypeDto;
import com.ringme.cms.model.kakoakcms.notification.NotiType;
import com.ringme.cms.service.kakoakcms.notification.NotiTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/noti-type")
public class NotiTypeController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    NotiTypeService service;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page, Model model,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "des", required = false) String des) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<NotiType> objectPage = service.get(service.processSearch(name, des), page, pageSize);
        List<NotiType> list = objectPage.toList();
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("des", des);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);
        model.addAttribute("title", messageSource.getMessage("title.noti-type", null, LocaleContextHolder.getLocale()));
        return "noti-type/index";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        try {
            log.info("id|{}", id);
            service.delete(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/noti-type/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        NotiType dto = new NotiType();
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.noti-type.create", null, LocaleContextHolder.getLocale()));
        return "noti-type/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        NotiType dto = service.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.noti-type.update", null, LocaleContextHolder.getLocale()));
        return "noti-type/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") NotiTypeDto dto, Errors error, RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            NotiType object = new NotiType();
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                object = service.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
            }
            object.setName(dto.getName());
            object.setDes(dto.getDes());
            service.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/noti-type/create";
            else
                return "redirect:/noti-type/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/noti-type/index";
    }
}