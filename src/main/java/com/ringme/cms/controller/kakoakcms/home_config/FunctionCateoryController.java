package com.ringme.cms.controller.kakoakcms.home_config;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.FunctionCategoryDto;
import com.ringme.cms.model.kakoakcms.home_config.FunctionCategory;
import com.ringme.cms.service.kakoakcms.FunctionCategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/function-category")
public class FunctionCateoryController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    FunctionCategoryService service;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@RequestParam(name = "titleCate", required = false) String title,
                             @RequestParam(name = "deeplink", required = false) String deeplink,
                             @RequestParam(name = "enable", required = false) Integer enable,
                             @RequestParam(name = "type", required = false) Integer type, Model model) {
        FunctionCategoryDto dto = service.processSearch(title, deeplink, enable, type);
        List<FunctionCategory> list = service.get(dto);
        model.addAttribute("title", title);
        model.addAttribute("deeplink", deeplink);
        model.addAttribute("enable", enable);
        model.addAttribute("type", type);
        model.addAttribute("models", list);
        model.addAttribute("title", messageSource.getMessage("title.function-category", null, LocaleContextHolder.getLocale()));
        return "function-category/index";
    }

    @GetMapping(value = {"/delete"})
    public String delete(@RequestParam(name = "id", required = false) Long id,
                         RedirectAttributes redirectAttributes) {
        log.info("id|{}", id);
        service.delete(id);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/function-category/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        FunctionCategory dto = new FunctionCategory();
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.function-category.create", null, LocaleContextHolder.getLocale()));
        return "function-category/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        FunctionCategory dto = service.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.function-category.update", null, LocaleContextHolder.getLocale()));
        return "function-category/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") FunctionCategoryDto dto, Errors error, RedirectAttributes redirectAttributes,
                       @RequestParam(name = "iconUpload", required = false) String iconUpload) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            FunctionCategory object = new FunctionCategory();
            if(dto.getId() == null) {
                object.setCreatedDate(new Date());
            } else {
                object = service.findById(dto.getId());
                object.setModifiedDate(new Date());
            }
            object.setTitle(dto.getTitle());
            object.setType(dto.getType());
            object.setEnable(dto.getEnable());
            object.setPriority(dto.getPriority());
            object.setDeeplink(dto.getDeeplink());
            Path fileIcon = uploadFile.createImageFile(iconUpload, "image");
            if (fileIcon != null) {
                object.setIcon(File.separator + fileIcon);
            }
            service.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/function-category/create";
            else
                return "redirect:/function-category/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/function-category/index";
    }
}
