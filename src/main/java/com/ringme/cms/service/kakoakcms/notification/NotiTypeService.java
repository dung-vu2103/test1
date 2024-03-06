package com.ringme.cms.service.kakoakcms.notification;

import com.ringme.cms.dto.kakoakcms.notification.NotiTypeDto;
import com.ringme.cms.model.kakoakcms.notification.NotiType;
import org.springframework.data.domain.Page;

public interface NotiTypeService {
    Page<NotiType> get(NotiTypeDto dto, int pageNo, int pageSize);
    NotiType findById(Long id);
    NotiTypeDto processSearch(String name, String des);
    void delete(Long id);
    void save(NotiType object);
}
