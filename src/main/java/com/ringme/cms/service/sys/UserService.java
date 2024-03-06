package com.ringme.cms.service.sys;

import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.model.sys.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Long checkChannel();
    String checkId();
    void saveUser(User user);

    Page<User> pageUser(int pageNo, int pageSize, Long id, String email, String name, String phone);

    void deleteUser(Long id) throws Exception;

    Optional<User> findByIdUser(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAllUserType();

    Optional<User> findUserByIdCheck(Long id);
    List<String> processAccountType(List<User> userList);

    String getTimeZone();

    String getUsername();

    Long getId();

    User getUser();

    List<AjaxSearchDto> ajaxSearchUser(String input);
}
