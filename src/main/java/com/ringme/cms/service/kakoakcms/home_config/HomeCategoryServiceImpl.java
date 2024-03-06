package com.ringme.cms.service.kakoakcms.home_config;

import com.ringme.cms.model.kakoakcms.home_config.HomeCategory;
import com.ringme.cms.repository.kakoakcms.home_config.HomeCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional(value = "primaryTransactionManager")
public class HomeCategoryServiceImpl implements HomeCategoryService {
    @Autowired
    HomeCategoryRepository repository;

    @Override
    public List<HomeCategory> get() {
        return repository.get();
    }

    @Override
    public void priority(int id, int priorityOld, int priorityNew) {
        repository.updateForOld(priorityOld, priorityNew);
        repository.updateById(id, priorityNew);
    }
}
