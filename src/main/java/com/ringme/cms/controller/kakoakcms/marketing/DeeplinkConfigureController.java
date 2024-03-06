package com.ringme.cms.controller.kakoakcms.marketing;

import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.model.kakoakcms.marketing.DeeplinkConfigure;
import com.ringme.cms.service.kakoakcms.marketing.DeeplinkConfigureService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/user/marketing/deeplink-configure")
public class DeeplinkConfigureController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    DeeplinkConfigureService deeplinkConfigureService;
    @GetMapping("/ajax-search")
    public ResponseEntity<List<AjaxSearchDto>> ajaxSearch(@RequestParam(name = "input_", required = false) String input) {
        return new ResponseEntity<>(deeplinkConfigureService.ajaxSearch(input), HttpStatus.OK);
    }
    @GetMapping(value = {"/", "/index", "/index/{page}", "/search", "/search/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "title", required = false) String title,
                             @RequestParam(name = "description", required = false) String description,
                             @RequestParam(name = "isActive", required = false) Integer isActive,
                             @RequestParam(name = "type", required = false) Integer type,
                             @RequestParam(name = "createdAt", required = false) String createdAt,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) throws UnsupportedEncodingException {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        String decodedDaterange = "";

        if (createdAt != null) {
            decodedDaterange = URLEncoder.encode(createdAt, "UTF-8");
            log.info("Time search encode: " + decodedDaterange);
        }

        Page<DeeplinkConfigure> objects = deeplinkConfigureService.getList(title, description, isActive, type, createdAt, page, pageSize);
        List<DeeplinkConfigure> deeplinkConfigures = objects.toList();
        model.addAttribute("name", title);
        model.addAttribute("description", description);
        model.addAttribute("isActive", isActive);
        model.addAttribute("type", type);
        model.addAttribute("createdAt", createdAt);
        model.addAttribute("time_start_encode", decodedDaterange);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objects.getTotalPages());
        model.addAttribute("totalItems", objects.getTotalElements());
        model.addAttribute("models", deeplinkConfigures);
        model.addAttribute("title", messageSource.getMessage("title.marketing.deeplink-configure", null, LocaleContextHolder.getLocale()));
        log.info("List Official Account|" + objects.toList());
        return "user/marketing/deeplink-configure/index";
    }
}
