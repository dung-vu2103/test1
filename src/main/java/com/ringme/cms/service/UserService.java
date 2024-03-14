package com.ringme.cms.service;

import com.ringme.cms.dto.kakoakcms.UserDto;
import com.ringme.cms.model.kakoakcms.User1;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDto processSearch(String name);
    Page<User1> getAll(UserDto userDto, int pageNO, int pageSize);
    User1 findById(Integer id);
    void save(User1 user1);
    void delete(Integer id);
}
