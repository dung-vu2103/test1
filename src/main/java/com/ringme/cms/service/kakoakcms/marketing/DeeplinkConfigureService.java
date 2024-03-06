package com.ringme.cms.service.kakoakcms.marketing;

import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.model.kakoakcms.marketing.DeeplinkConfigure;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeeplinkConfigureService {
    Page<DeeplinkConfigure> getList(String title, String description, Integer isActive, Integer type, String createdAt, int pageNo, int pageSize);
    List<AjaxSearchDto> ajaxSearch(String input);
    DeeplinkConfigure findById(Long id);
}
