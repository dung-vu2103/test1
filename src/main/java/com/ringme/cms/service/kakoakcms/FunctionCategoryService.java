package com.ringme.cms.service.kakoakcms;

import com.ringme.cms.dto.kakoakcms.FunctionCategoryDto;
import com.ringme.cms.model.kakoakcms.home_config.FunctionCategory;

import java.util.List;

public interface FunctionCategoryService {
    List<FunctionCategory> get(FunctionCategoryDto dto);
    FunctionCategory findById(Long id);
    FunctionCategoryDto processSearch(String title, String deeplink, Integer enable, Integer type);
    void delete(Long id);
    void save(FunctionCategory object);
}
