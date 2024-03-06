package com.ringme.cms.service.sys;

import com.ringme.cms.model.sys.Role;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAllRole();

    Page<Role> pageRole(int pageNo, int pageSize);

    Optional<Role> findRoleById(Long id);

    void saveRole(Role role) throws Exception;

    void deleteRole(Long id) throws Exception;

    Optional<Role> findByRoleName(String roleName);

    List<Role> findAllRoleNotInListIdRole(List<Long> idRole);
}
