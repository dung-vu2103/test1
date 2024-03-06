package com.ringme.cms.controller.kakoakcms.sticker;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.sticker.StickerDto;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.service.kakoakcms.sticker.StickerService;
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
@RequestMapping("/sticker")
public class StickerController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    StickerService service;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "name", required = false) String name, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        StickerDto dto = service.processSearch(name);
        Page<Sticker> objectPage = service.get(dto, page, pageSize);
        List<Sticker> list = objectPage.toList();
        model.addAttribute("currentPage", page);
        model.addAttribute("name", name);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);
        model.addAttribute("title", messageSource.getMessage("title.sticker", null, LocaleContextHolder.getLocale()));
        return "sticker/index";
    }

    @GetMapping("/view/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        Sticker object = service.findById(id);
        model.addAttribute("model", object);
        model.addAttribute("titleDetail", messageSource.getMessage("title.view-detail", null, LocaleContextHolder.getLocale()));
        return "sticker/index::view_detail";
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
        return "redirect:/sticker/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        Sticker dto = new Sticker();
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker.create", null, LocaleContextHolder.getLocale()));
        return "sticker/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        Sticker dto = service.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker.update", null, LocaleContextHolder.getLocale()));
        return "sticker/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") StickerDto dto, Errors error, RedirectAttributes redirectAttributes,
                       @RequestParam(name = "iconUpload", required = false) String iconUpload,
                       @RequestParam(name = "previewUpload", required = false) String previewUpload) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            Sticker object = new Sticker();
            if(dto.getId() == null) {
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                object = service.findById(dto.getId());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
            }

            Path fileIcon = uploadFile.createImageFile(iconUpload, "image");
            if (fileIcon != null) {
                object.setIconUrl(File.separator + fileIcon);
            }
            Path fileNameBanner = uploadFile.createImageFile(previewUpload, "image");
            if (fileNameBanner != null) {
                object.setPreviewUrl(File.separator + fileNameBanner);
            }

            object.setName(dto.getName());
            object.setIorder(dto.getIorder());
            object.setIsNew(dto.getIsNew());
            object.setSticky(dto.getSticky());
            object.setActive(dto.getActive());
            service.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/sticker/create";
            else
                return "redirect:/sticker/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/sticker/index";
    }
}
