package com.ringme.cms.service.kakoakcms.notification;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.notification.NotiTypeDto;
import com.ringme.cms.model.kakoakcms.notification.NotiType;
import com.ringme.cms.repository.kakoakcms.notification.NotiTypeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@Transactional(value = "primaryTransactionManager")
public class NotiTypeServiceImpl implements NotiTypeService {
    @Autowired
    NotiTypeRepository repository;
    @Override
    public Page<NotiType> get(NotiTypeDto dto, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.get(dto.getName(), dto.getDes(), pageable);
    }
    @Override
    public NotiType findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public NotiTypeDto processSearch(String name, String des) {
        NotiTypeDto dto = new NotiTypeDto();
        dto.setName(Helper.processStringSearch(name));
        dto.setDes(Helper.processStringSearch(des));
        return dto;
    }
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void save(NotiType object) {
        repository.save(object);
    }
}
