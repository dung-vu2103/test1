package com.ringme.cms.service.kakoakcms.sticker;

import com.ringme.cms.dto.kakoakcms.sticker.StickerDto;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import org.springframework.data.domain.Page;

public interface StickerService {
    Page<Sticker> get(StickerDto dto, int pageNo, int pageSize);
    Sticker findById(Long id);
    StickerDto processSearch(String name);
    void delete(Long id);
    void save(Sticker sticker);
}
