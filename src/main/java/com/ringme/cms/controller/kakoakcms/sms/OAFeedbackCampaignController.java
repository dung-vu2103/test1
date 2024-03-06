package com.ringme.cms.controller.kakoakcms.sms;

import com.google.gson.Gson;
import com.ringme.cms.dto.kakoakcms.sms.CronParamDto;
import com.ringme.cms.dto.kakoakcms.sms.OAFeedbackCampaignDto;
import com.ringme.cms.model.kakoakcms.sms.OAFeedbackCampaign;
import com.ringme.cms.service.kakoakcms.sms.OAFeedbackCampaignService;
import com.ringme.cms.service.sys.UserService;
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
@RequestMapping("/sms")
public class OAFeedbackCampaignController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    OAFeedbackCampaignService service;

    @Autowired
    UserService userService;

    @GetMapping(value = {"/", "/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "titleSMS", required = false) String titleSMS,
                             @RequestParam(name = "officialAccountId", required = false) String officialAccountId,
                             @RequestParam(name = "inputType", required = false) String inputType,
                             @RequestParam(name = "msgType", required = false) String msgType,
                             @RequestParam(name = "status", required = false) Integer status,
                             @RequestParam(name = "processStatus", required = false) Integer processStatus,
                             @RequestParam(name = "startAt", required = false) String startAt,
                             @RequestParam(name = "endAt", required = false) String endAt, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        OAFeedbackCampaignDto dto = service.processSearch(titleSMS, officialAccountId, inputType, msgType, status, processStatus, startAt, endAt);
        Page<OAFeedbackCampaign> objectPage = service.get(dto, page, pageSize);
        List<OAFeedbackCampaign> list = objectPage.toList();
        service.handlerCronParam(list);
        model.addAttribute("currentPage", page);
        model.addAttribute("titleSMS", titleSMS);
        model.addAttribute("officialAccountId", officialAccountId);
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
        model.addAttribute("title", messageSource.getMessage("title.sms.oa-feedback-campaign", null, LocaleContextHolder.getLocale()));
        return "sms/index";
    }

    @GetMapping("/view/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        OAFeedbackCampaign object = service.findById(id);
        service.handlerCronParamObj(object);
        model.addAttribute("model", object);
        model.addAttribute("titleDetail", messageSource.getMessage("title.view-detail", null, LocaleContextHolder.getLocale()));
        return "sms/index::view_detail";
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
        return "redirect:/sms/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/create")
    public String create(Model model) {
        OAFeedbackCampaignDto dto = new OAFeedbackCampaignDto();
        dto.setCronParamDto(new CronParamDto());
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sms.create", null, LocaleContextHolder.getLocale()));
        return "sms/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        OAFeedbackCampaignDto dto = new OAFeedbackCampaignDto(service.findById(id));
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sms.update", null, LocaleContextHolder.getLocale()));
        log.info("UPDATEeee|" + dto);
        return "sms/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") OAFeedbackCampaignDto dto, Errors error,
                       @RequestParam(name = "excelUpload", required = false) MultipartFile filePath,
                       @RequestParam(name = "cronType", required = false) String cronType,
                       @RequestParam(name = "cronMin", required = false) String cronMin,
                       @RequestParam(name = "cronHour", required = false) String cronHour,
                       @RequestParam(name = "cronWeekDay[]", required = false) List<String> cronWeekDay,
                       @RequestParam(name = "cronMonthDay[]", required = false) List<String> cronMonthDay,
                       RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);
        if(!error.hasErrors()){
            service.save(dto, filePath, cronType, cronMin, cronHour, cronWeekDay, cronMonthDay);
        } else {
            log.error("ERROR|Save|" + error);
            if(dto.getId() == null)
                return "redirect:/sms/create";
            else
                return "redirect:/sms/update/" + dto.getId();
        }
        if(dto.getId() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/sms/index";
    }
}
