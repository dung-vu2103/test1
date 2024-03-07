package com.ringme.cms.service;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoakcms.MinDto;
import com.ringme.cms.model.kakoakcms.Min;
import com.ringme.cms.repository.kakoakcms.MinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class MinServiceImp implements MinService {
    @Autowired
    MinRepository adminRepository;
    @Override
    public List<Min> getAll() {
        return adminRepository.search();
    }

    @Override
    public MinDto processSearch(String name) {
        MinDto minDto=new MinDto();
        minDto.setName(Helper.processStringSearch(name));
        return minDto;
    }

    @Override
    public Page<Min> get(MinDto dto, int pageNO, int pageSize) {
        Pageable pageable= PageRequest.of(pageNO -1,pageSize);
        return adminRepository.get(dto.getName(), pageable);
    }

    @Override
    public Min findById(Integer id) {
      return adminRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        adminRepository.deleteById(id);
    }

    @Override
    public void save(Min min) {
        adminRepository.save(min);
    }


}
