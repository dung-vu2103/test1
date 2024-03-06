package com.ringme.cms.service.kakoakcms;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.FunctionCategoryDto;
import com.ringme.cms.dto.kakoakcms.sticker.StickerDto;
import com.ringme.cms.model.kakoakcms.home_config.FunctionCategory;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.repository.kakoakcms.FunctionCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional(value = "primaryTransactionManager")
public class FunctionCategoryServiceImpl implements FunctionCategoryService {
    @Autowired
    FunctionCategoryRepository repository;
    @Override
    public List<FunctionCategory> get(FunctionCategoryDto dto) {
        return repository.get(dto.getTitle(), dto.getDeeplink(), dto.getEnable(), dto.getType());
    }
    @Override
    public FunctionCategory findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public FunctionCategoryDto processSearch(String title, String deeplink, Integer enable, Integer type) {
        FunctionCategoryDto dto = new FunctionCategoryDto();
        dto.setTitle(Helper.processStringSearch(title));
        dto.setDeeplink(Helper.processStringSearch(deeplink));
        dto.setEnable(enable);
        dto.setType(type);
        return dto;
    }
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void save(FunctionCategory object) {
        repository.save(object);
    }
}
