package com.ringme.cms.service.kakoakcms.sticker;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.sticker.StickerDto;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.repository.kakoakcms.sticker.StickerRepository;
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
public class StickerServiceImpl implements StickerService {
    @Autowired
    StickerRepository repository;
    @Override
    public Page<Sticker> get(StickerDto dto, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.get(dto.getName(), pageable);
    }
    @Override
    public Sticker findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public StickerDto processSearch(String name) {
        StickerDto dto = new StickerDto();
        dto.setName(Helper.processStringSearch(name));
        return dto;
    }
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void save(Sticker sticker) {
        repository.save(sticker);
    }
}
