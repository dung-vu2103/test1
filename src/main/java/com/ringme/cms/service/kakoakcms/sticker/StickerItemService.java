package com.ringme.cms.service.kakoakcms.sticker;

import com.ringme.cms.model.kakoakcms.sticker.StickerItem;
import org.springframework.data.domain.Page;

public interface StickerItemService {
    Page<StickerItem> get(long stickerId, int pageNo, int pageSize);
    StickerItem findById(Long id);
    void delete(Long id);
    void save(StickerItem obj);
}
