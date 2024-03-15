package com.ringme.cms.controller.kakoakcms;

import com.ringme.cms.common.UpFile;
import com.ringme.cms.dto.kakoakcms.MinDto;
import com.ringme.cms.dto.kakoakcms.sticker.StickerDto;
import com.ringme.cms.model.kakoakcms.Min;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.service.MinService;
import lombok.extern.java.Log;
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
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("/admin11")
public class Min1Controller {
        @Autowired
        private MessageSource messageSource;
        @Autowired
        MinService adminService;
        @Autowired
        UpFile upFile;

        @GetMapping(value={"/get","/get/{page}"})
        private String index1(@PathVariable(required = false) Integer page,
                              @RequestParam(name = "pageSize", required = false) Integer pageSize,
                              @RequestParam(value = "name", required = false) String name
                , Model model) {
            if (page == null) {
                page = (Integer) 1;
            }
            if (pageSize == null) {
                pageSize = (Integer) 10;
            }
            MinDto minDto = adminService.processSearch(name);
            Page<Min> oblectPage = adminService.get(minDto, page, pageSize);
            List<Min> admins = oblectPage.toList();
            log.info("listttt" + admins);
            model.addAttribute("currentPage", page);
            model.addAttribute("name", name);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", Optional.of(oblectPage.getTotalPages()));
            model.addAttribute("totalItems", Optional.of(oblectPage.getTotalElements()));
            model.addAttribute("admins", admins);

            return "index1";
        }

        @GetMapping("/view/{id}")
        public String detail(@PathVariable(name = "id") Integer id, Model model) {
            Min object = adminService.findById(id);
            model.addAttribute("admin", object);

            return "index1::view_detail";
        }
    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Model model) {

        Min min = adminService.findById(id);
        model.addAttribute("model",min);
        return "form";
    }
    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Integer id,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = (Integer) 1;
        if(pageSize == null)
            pageSize = (Integer) 10;
        try {
            adminService.delete(id);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("title.delete.success", null,
                            LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/admin11/get/";
    }
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") MinDto dto, Errors error,
                       @RequestParam(name = "iconUpload", required = false) String iconUpload
                       ,RedirectAttributes redirectAttributes
                      ) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            Min object = new Min();
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                object = adminService.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
            }
            Path icon=upFile.createImageFile(iconUpload,"image");
            if (icon != null) {
                object.setImg(File.separator +icon);
            }

            object.setName(dto.getName());
            log.info("object1111111111111111 " + object);
            adminService.save(object);


        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/admin11/create";
            else
                return "redirect:/admin11/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/admin11/get";
    }
    @GetMapping("/create")
    public String create(Model model) {
        Min dto = new Min();
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker.create", null, LocaleContextHolder.getLocale()));
        return "form";
    }

}



