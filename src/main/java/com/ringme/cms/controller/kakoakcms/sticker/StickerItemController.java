package com.ringme.cms.controller.kakoakcms.sticker;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.sticker.StickerItemDto;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.model.kakoakcms.sticker.StickerItem;
import com.ringme.cms.service.kakoakcms.sticker.StickerItemService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/sticker-item")
public class StickerItemController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    StickerItemService service;

    @Autowired
    StickerService stickerService;

    @Autowired
    UploadFile uploadFile;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "stickerId") long stickerId, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<StickerItem> objectPage = service.get(stickerId, page, pageSize);
        Sticker sticker = stickerService.findById(stickerId);
        model.addAttribute("currentPage", page);
        model.addAttribute("stickerId", stickerId);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        model.addAttribute("sticker", sticker);
        model.addAttribute("title", messageSource.getMessage("title.stickerItem", null, LocaleContextHolder.getLocale()));
        return "sticker-item/index";
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
        log.info("id|{}", id);
        service.delete(id);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/sticker-item/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(@RequestParam(name = "stickerId") long stickerId, Model model) {
        StickerItem dto = new StickerItem();
        dto.setStickerId(stickerId);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker-item.create", null, LocaleContextHolder.getLocale()));
        return "sticker-item/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        StickerItem dto = service.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker-item.update", null, LocaleContextHolder.getLocale()));
        return "sticker-item/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") StickerItemDto dto, Errors error, RedirectAttributes redirectAttributes,
                       @RequestParam(name = "imageUrlUpload", required = false) MultipartFile imageUrlUpload,
                       @RequestParam(name = "voiceUrlUpload", required = false) MultipartFile voiceUrlUpload) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            StickerItem object = new StickerItem();
            if(dto.getId() == null) {
                object.setCreatedDate(new Date());
            } else {
                object = service.findById(dto.getId());
                object.setModifiedDate(new Date());
            }
            object.setStickerId(dto.getStickerId());
            object.setIorder(dto.getIorder());
            object.setActive(dto.getActive());
            String[] fileNameImage = uploadFile.fileName(imageUrlUpload, "stickers");
            if(fileNameImage != null && (dto.getId() == null || !fileNameImage[2].equals(dto.getImageUrl()))) {
                object.setImageUrl("/" + fileNameImage[1]);
                uploadFile.upload(imageUrlUpload, fileNameImage);
            }

            String[] fileNamevoice = uploadFile.fileName(voiceUrlUpload, "stickers");
            if(fileNamevoice != null && (dto.getId() == null || !fileNamevoice[2].equals(dto.getVoiceUrl()))) {
                object.setVoiceUrl("/" + fileNamevoice[1]);
                uploadFile.upload(voiceUrlUpload, fileNamevoice);
            }
            service.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/sticker-item/create?stickerId=" + dto.getStickerId();
            else
                return "redirect:/sticker-item/update/" + dto.getId() + "?stickerId=" + dto.getStickerId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/sticker-item/index?stickerId=" + dto.getStickerId();
    }
}
