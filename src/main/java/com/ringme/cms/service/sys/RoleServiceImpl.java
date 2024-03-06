package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.Role;
import com.ringme.cms.repository.sys.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@org.springframework.transaction.annotation.Transactional(value = "primaryTransactionManager")
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> pageRole(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return roleRepository.findAll(pageable);
    }

    @Override
    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void saveRole(Role role) throws Exception {
        try {
            role.setRoleName("ROLE_" + role.getRoleName().toUpperCase());
            roleRepository.save(role);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteRole(Long id) throws Exception {
        try {
            roleRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName("ROLE_" + roleName.toUpperCase());
    }

    @Override
    public List<Role> findAllRoleNotInListIdRole(List<Long> idRole) {
        return roleRepository.findAllRoleNotInListIdRole(idRole);
    }
}
