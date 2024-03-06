package com.ringme.cms.service.kakoakcms.report;

import com.ringme.cms.dto.kakoakcms.report.TelemorReportDailyNewDto;
import com.ringme.cms.model.kakoakcms.report.TelemorReportDailyNew;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public interface TelemorReportDailyNewService {
    Page<TelemorReportDailyNew> page(int pageNo, int pageSize, TelemorReportDailyNewDto dto);
    TelemorReportDailyNewDto handlerSearch(String dateRanger);
    TelemorReportDailyNew findById(Long id);
    void exportExcel(String dateRanger, HttpServletResponse response);
}