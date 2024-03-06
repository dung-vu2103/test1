package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.UserRole;

import java.util.List;

public interface UserRoleService {
    void deleteById(Long id) throws Exception;

    void addUserRole(UserRole userRole);

    List<UserRole> findAllUserRoleByIdUser(Long id);

    void saveAllUserRole(List<UserRole> userRoles) throws Exception;

    void deleteUserRoleById(List<Long> ids) throws Exception;
}
