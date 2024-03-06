package com.ringme.cms.service.kakoakcms.report;

import com.ringme.cms.common.ExportExcel;
import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.report.TelemorReportDailyNewDto;
import com.ringme.cms.model.kakoakcms.report.TelemorReportDailyNew;
import com.ringme.cms.repository.kakoakcms.report.TelemorReportDailyNewRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class TelemorReportDailyNewServiceImpl implements TelemorReportDailyNewService {
    @Autowired
    private TelemorReportDailyNewRepository repository;
    @Autowired
    private ExportExcel export;
    @Override
    public Page<TelemorReportDailyNew> page(int pageNo, int pageSize, TelemorReportDailyNewDto dto) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        String[] dateRangers = Helper.reportDateV2(dto.getDateReport());
        log.info("start date|" + dateRangers[0]);
        log.info("end date|" + dateRangers[1]);
        return repository.getPage(dateRangers[0], dateRangers[1], pageable);
    }

    @Override
    public TelemorReportDailyNewDto handlerSearch(String dateRanger) {
        TelemorReportDailyNewDto dto = new TelemorReportDailyNewDto();
        dto.setDateReport(dateRanger);
        return dto;
    }

    @Override
    public TelemorReportDailyNew findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void exportExcel(String dateRanger, HttpServletResponse response) {
        String[] dateRangers = Helper.reportDateV2(dateRanger);
        List<TelemorReportDailyNew> list = repository.getList(dateRangers[0], dateRangers[1]);
        log.info("listtt|" + list);
        ModelMapper modelMapper = new ModelMapper();
        List<TelemorReportDailyNewDto> listDto = new ArrayList<>();
        for (TelemorReportDailyNew o : list) {
            TelemorReportDailyNewDto dto = modelMapper.map(o, TelemorReportDailyNewDto.class);
            listDto.add(dto);
        }
        log.info("listDto|" + listDto);
        String[] headers = { "Date report", "New user", "Active user", "Call out user", "Call out minute", "SMS out user",
        "Number SMS out", "Call data user", "Call data minute", "New user in month", "Active user in month", "Call out user in month",
        "Call out minute in month", "SMS out user in month", "Number SMS out in month", "Call data user in month",
        "Call data minute in month", "Total user"};
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String title = "Daily Report";
        export.export(listDto, headers, response, formatter.format(new Date()), title);
    }
}
