package com.ringme.cms.service;

import com.ringme.cms.dto.kakoakcms.MinDto;
import com.ringme.cms.model.kakoakcms.Min;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MinService {
    List<Min> getAll();
    MinDto processSearch(String name);
    Page<Min> get(MinDto dto,int pageNO,int pageSize);
    Min findById(Integer id);
    void delete(Integer id);
    void save(Min min);
}
