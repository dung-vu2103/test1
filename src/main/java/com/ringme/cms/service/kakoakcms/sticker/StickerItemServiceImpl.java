package com.ringme.cms.service.kakoakcms.sticker;

import com.ringme.cms.model.kakoakcms.sticker.StickerItem;
import com.ringme.cms.repository.kakoakcms.sticker.StickerItemRepository;
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
public class StickerItemServiceImpl implements StickerItemService {
    @Autowired
    StickerItemRepository repository;
    @Override
    public Page<StickerItem> get(long stickerId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return repository.get(stickerId, pageable);
    }
    @Override
    public StickerItem findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void save(StickerItem obj) {
        repository.save(obj);
    }
}
