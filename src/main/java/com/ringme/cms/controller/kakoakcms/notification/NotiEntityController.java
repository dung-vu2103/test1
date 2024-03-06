package com.ringme.cms.controller.kakoakcms.notification;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoakcms.notification.DeeplinkConfigureDto;
import com.ringme.cms.dto.kakoakcms.notification.NotiEntityDto;
import com.ringme.cms.dto.kakoakcms.sms.CronParamDto;
import com.ringme.cms.model.kakoakcms.marketing.DeeplinkConfigure;
import com.ringme.cms.model.kakoakcms.notification.NotiEntity;
import com.ringme.cms.service.kakoakcms.marketing.DeeplinkConfigureService;
import com.ringme.cms.service.kakoakcms.notification.NotiEntityService;
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
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/noti-entity")
public class NotiEntityController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    NotiEntityService service;

    @Autowired
    UploadFile uploadFile;

    @Autowired
    DeeplinkConfigureService deeplinkConfigureService;

    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page, Model model,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "title2", required = false) String title,
                             @RequestParam(name = "inputType", required = false) String inputType,
                             @RequestParam(name = "msgType", required = false) String msgType,
                             @RequestParam(name = "status", required = false) Integer status,
                             @RequestParam(name = "processStatus", required = false) Integer processStatus,
                             @RequestParam(name = "startAt", required = false) String startAt,
                             @RequestParam(name = "endAt", required = false) String endAt) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<NotiEntity> objectPage = service.get(service.processSearch(title, inputType, msgType, status, processStatus, startAt, endAt), page, pageSize);
        List<NotiEntity> list = objectPage.toList();
        service.handlerCronParam(list);
        model.addAttribute("currentPage", page);
        model.addAttribute("title2", title);
        model.addAttribute("inputType", inputType);
        model.addAttribute("msgType", msgType);
        model.addAttribute("status", status);
        model.addAttribute("processStatus", processStatus);
        model.addAttribute("startAt", startAt);
        model.addAttribute("endAt", endAt);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", list);
        model.addAttribute("title", messageSource.getMessage("title.noti-entity", null, LocaleContextHolder.getLocale()));
        return "noti-entity/index";
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
        return "redirect:/noti-entity/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        NotiEntityDto dto = new NotiEntityDto();
        dto.setCronParamDto(new CronParamDto());
        dto.setDeeplinkConfigureDto(new DeeplinkConfigureDto());
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.noti-entity.create", null, LocaleContextHolder.getLocale()));
        return "noti-entity/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        NotiEntity notiEntity = service.findById(id);
        DeeplinkConfigure deeplinkConfigure = deeplinkConfigureService.findById(Long.valueOf(notiEntity.getDeeplinkConfigureId()));
        String linkTo = service.getLinkTo(deeplinkConfigure.getDeeplink(), notiEntity.getDeeplinkParams());
        log.info("linkToo|" + linkTo);
        NotiEntityDto dto = new NotiEntityDto(notiEntity, deeplinkConfigure, linkTo);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.noti-entity.update", null, LocaleContextHolder.getLocale()));
        return "noti-entity/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") NotiEntityDto dto, Errors error, RedirectAttributes redirectAttributes,
                       @RequestParam(name = "excelUpload", required = false) MultipartFile filePath,
                       @RequestParam(name = "cronType", required = false) String cronType,
                       @RequestParam(name = "cronMin", required = false) String cronMin,
                       @RequestParam(name = "cronHour", required = false) String cronHour,
                       @RequestParam(name = "cronWeekDay[]", required = false) List<String> cronWeekDay,
                       @RequestParam(name = "cronMonthDay[]", required = false) List<String> cronMonthDay,
                       @RequestParam(name = "thumbUpload", required = false) String thumbUpload) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            service.save(dto, filePath, cronType, cronMin, cronHour, cronWeekDay, cronMonthDay, thumbUpload);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/noti-entity/create";
            else
                return "redirect:/noti-entity/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/noti-entity/index";
    }

    @GetMapping("/view/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        NotiEntity object = service.findById(id);
        service.handlerCronParamObj(object);
        model.addAttribute("model", object);
        model.addAttribute("titleDetail", messageSource.getMessage("title.view-detail", null, LocaleContextHolder.getLocale()));
        return "noti-entity/index::view_detail";
    }
}