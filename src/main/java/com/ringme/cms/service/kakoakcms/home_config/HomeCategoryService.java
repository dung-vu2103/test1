package com.ringme.cms.service.kakoakcms.home_config;

import com.ringme.cms.model.kakoakcms.home_config.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    List<HomeCategory> get();
    void priority(int id, int priorityOld, int priorityNew);
}
