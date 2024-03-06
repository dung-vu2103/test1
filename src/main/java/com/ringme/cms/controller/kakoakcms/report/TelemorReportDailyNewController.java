package com.ringme.cms.controller.kakoakcms.report;

import com.ringme.cms.common.ExportExcel;
import com.ringme.cms.dto.kakoakcms.report.TelemorReportDailyNewDto;
import com.ringme.cms.model.kakoakcms.report.TelemorReportDailyNew;
import com.ringme.cms.service.kakoakcms.report.TelemorReportDailyNewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/report/daily-new")
public class TelemorReportDailyNewController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private TelemorReportDailyNewService service;
    @Autowired
    ExportExcel excel;
    @GetMapping(value = {"", "/{page}", "/index", "/index/{page}"})
    public String getPage(@PathVariable(required = false) Integer page,
                          @RequestParam(name = "dateRanger", required = false) String dateRanger,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize, Model model) throws UnsupportedEncodingException {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        if(dateRanger == null || dateRanger.equals("")) {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String end = formatter.format(new Date());
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -90);
            String start = formatter.format(cal.getTime());
            dateRanger = start + " - " + end;
        }
        TelemorReportDailyNewDto dto = service.handlerSearch(dateRanger);
        Page<TelemorReportDailyNew> objects = service.page(page, pageSize, dto);
        List<TelemorReportDailyNew> list = objects.toList();
        model.addAttribute("dateRanger", dateRanger);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objects.getTotalPages());
        model.addAttribute("totalItems", objects.getTotalElements());
        model.addAttribute("models", list);
        model.addAttribute("title", messageSource.getMessage("title.report.daily-new", null, LocaleContextHolder.getLocale()));
        log.info("List TelemorReportDailyNew|" + objects.toList());
        return "report/daily-new";
    }

    @GetMapping(value = {"/view/{id}"})
    public String view(@PathVariable(required = false) Long id, Model model) {
        TelemorReportDailyNew object = service.findById(id);
        model.addAttribute("model", object);
        log.info(object);
        return "report/daily-new::view";
    }

    @PostMapping("/export")
    public void export(@RequestParam(name = "dateRanger", required = false) String dateRanger,
                       HttpServletResponse response) {
        response = excel.setResponse(response);
        service.exportExcel(dateRanger, response);
    }
}
