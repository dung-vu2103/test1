package com.ringme.cms.service.kakoakcms.marketing;

import com.ringme.cms.common.Helper;
import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.model.kakoakcms.marketing.DeeplinkConfigure;
import com.ringme.cms.repository.kakoakcms.marketing.DeeplinkConfigureRepository;
import com.ringme.cms.repository.sys.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
@Transactional(value = "primaryTransactionManager")
public class DeeplinkConfigureServiceImpl implements DeeplinkConfigureService {
    @Autowired
    DeeplinkConfigureRepository deeplinkConfigureRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UploadFile uploadFile;

    @Override
    public Page<DeeplinkConfigure> getList(String title, String description, Integer isActive, Integer type, String createdAt, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Date startTime = null;
        Date endTime = null;

        if (title != null) {
            if (title.trim().equals("")) {
                title = null;
            } else {
                title = title.trim().replaceAll("\s+", " ");
            }
        }

        if (description != null) {
            if (description.trim().equals("")) {
                description = null;
            } else {
                description = description.trim().replaceAll("\s+", " ");
            }
        }

        if (isActive != null) {
            if (isActive == 1) {
                isActive = 1;
            } else if (isActive == 0){
                isActive = 0;
            } else {
                isActive = null;
            }
        }

        if (type != null) {
            if (type == 1) {
                type = 1;
            } else if (type == 2){
                type = 2;
            } else if (type == 3){
                type = 3;
            } else if (type == 4){
                type = 4;
            } else if (type == 5){
                type = 5;
            } else if (type == 6){
                type = 6;
            } else {
                type = null;
            }
        }

        if (createdAt != null) {
            if (!createdAt.trim().equals("")) {
                String[] parts = createdAt.split(" - ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate zonedDateTime = LocalDate.parse(parts[0], formatter);
                LocalDate zonedDateTime2 = LocalDate.parse(parts[1], formatter);
                startTime = Date.from(zonedDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
                endTime = Date.from(zonedDateTime2.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        }
        log.info("===publishedTime Search===>startTime2 {} endTime2 {}", startTime, endTime);

        return deeplinkConfigureRepository.getList(title, description, isActive, type, startTime, endTime, pageable);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearch(String input) {
        return Helper.listAjax(deeplinkConfigureRepository.ajaxSearch(Helper.processStringSearch(input)), 1);
    }

    @Override
    public DeeplinkConfigure findById(Long id) {
        return deeplinkConfigureRepository.findById(id).orElse(null);
    }
}
