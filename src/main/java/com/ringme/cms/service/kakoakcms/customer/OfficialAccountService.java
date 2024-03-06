package com.ringme.cms.service.kakoakcms.customer;

import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.dto.kakoakcms.customer.OfficialAccountDto;
import com.ringme.cms.model.kakoakcms.customer.OfficialAccount;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OfficialAccountService {
    Page<OfficialAccount> getListOfficialAccount(String name, Integer status, String createdAt, int pageNo, int pageSize);

    OfficialAccount findById(Long id);

    void save(OfficialAccount object);

    void save(OfficialAccountDto dto, String statusForm, List<String> listUser, String thumbUpload);

    void delete(Long id);
    List<AjaxSearchDto> ajaxSearch(String input);
}
