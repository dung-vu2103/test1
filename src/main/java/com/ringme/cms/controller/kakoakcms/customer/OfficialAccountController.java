package com.ringme.cms.controller.kakoakcms.customer;

import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.dto.kakoakcms.customer.OfficialAccountDto;
import com.ringme.cms.model.kakoakcms.customer.OfficialAccount;
import com.ringme.cms.service.kakoakcms.customer.OfficialAccountService;
import com.ringme.cms.service.sys.UserService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.internal.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/user/customer-info/official-account")
public class OfficialAccountController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    OfficialAccountService customerInfoService;

    @Autowired
    UserService userService;

    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(customerInfoService.ajaxSearch(input), HttpStatus.OK);
    }

    @GetMapping(value = {"/", "/index", "/index/{page}", "/search", "/search/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "status", required = false) Integer status,
                             @RequestParam(name = "createdAt", required = false) String createdAt,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) throws UnsupportedEncodingException {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        String decodedDaterange = "";

        // Giải mã giá trị daterange
        if (createdAt != null) {
            decodedDaterange = URLEncoder.encode(createdAt, "UTF-8");
            log.info("Time search encode: " + decodedDaterange);
        }

        Page<OfficialAccount> objects = customerInfoService.getListOfficialAccount(name, status, createdAt, page, pageSize);
        List<OfficialAccount> officialAccount = objects.toList();
        model.addAttribute("name", name);
        model.addAttribute("iconCode", status);
        model.addAttribute("createdAt", createdAt);
        model.addAttribute("time_start_encode", decodedDaterange);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objects.getTotalPages());
        model.addAttribute("totalItems", objects.getTotalElements());
        model.addAttribute("models", officialAccount);
        model.addAttribute("title", messageSource.getMessage("title.customer-info.official-account", null, LocaleContextHolder.getLocale()));
        log.info("List Official Account|" + objects.toList());
        return "user/customer-info/official-account/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        OfficialAccount dto = new OfficialAccount();
        model.addAttribute("model", dto);
        model.addAttribute("statusForm", "0");
        model.addAttribute("title", messageSource.getMessage("title.customer-info.official-account.create", null, LocaleContextHolder.getLocale()));
        return "user/customer-info/official-account/create";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        OfficialAccount dto = customerInfoService.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("statusForm", "1");
        model.addAttribute("title", messageSource.getMessage("title.customer-info.official-account.update", null, LocaleContextHolder.getLocale()));
        log.info("UPDATE ICON PRIZE|" + dto);
        return "user/customer-info/official-account/update";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable(name = "id") Long id, Model model) {
        OfficialAccount object = customerInfoService.findById(id);
        model.addAttribute("model", object);
        model.addAttribute("id", id);
        model.addAttribute("titleDetail", messageSource.getMessage("title.customer-info.official-account.detail", null, LocaleContextHolder.getLocale()));
        log.info("DETAIL|" + object);
        String view = "user/customer-info/official-account/index::OfficialAccount_view";
        return view;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") OfficialAccountDto dto, Errors error,
                       @ModelAttribute("statusForm") String statusForm,
                       @RequestParam(name = "listUser", required = false) List<String> listUser,
                       @RequestParam String thumbUpload,
                       RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);

        if(!error.hasErrors()){
            customerInfoService.save(dto, statusForm, listUser, thumbUpload);
        } else {
            log.error("ERROR|Save|" + error);
            if(statusForm.equals("0"))
                return "redirect:/user/customer-info/official-account/create";
            if(statusForm.equals("1")){
                String redirectUrl = "/user/customer-info/official-account/update/" + dto.getId();
                return "redirect:" + redirectUrl;
            }
        }
        if(statusForm.equals("0"))
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        if(statusForm.equals("1"))
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/user/customer-info/official-account/index";
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
            customerInfoService.delete(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/user/customer-info/official-account/index/" + page + "?pageSize=" + pageSize;
    }

    @GetMapping("/ajax-search-user")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearchArtist(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(userService.ajaxSearchUser(input), HttpStatus.OK);
    }
}
