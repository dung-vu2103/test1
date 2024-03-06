package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.Role;
import com.ringme.cms.model.sys.Router;
import com.ringme.cms.model.sys.RouterRole;
import com.ringme.cms.repository.sys.RoleRepository;
import com.ringme.cms.repository.sys.RouterRepository;
import com.ringme.cms.repository.sys.RouterRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@org.springframework.transaction.annotation.Transactional(value = "primaryTransactionManager")
public class RoleRouterServiceImpl implements RoleRouterService {
    @Autowired
    RouterRoleRepository routerRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RouterRepository routerRepository;

    @Override
    public List<RouterRole> findAllRouterRole() {
        return routerRoleRepository.findAll();
    }

    @Override
    public List<RouterRole> findAllRouterRoleByRoleId(Long roleId) {
        return routerRoleRepository.findAllRouterRoleByRoleId(roleId);
    }

    @Override
    public List<RouterRole> findAllRouterRoleByListRoleId(List<Long> roleIds) {
        return routerRoleRepository.findAllRouterRoleByListRoleId(roleIds);
    }

    @Override
    public void saveRoleRouter(RouterRole routerRole) {
        routerRoleRepository.save(routerRole);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteRoleRouter(List<Long> id) throws Exception {
        for (Long i : id) {
            try {
                routerRoleRepository.deleteById(i);
            } catch (Exception e) {
                throw new Exception();
            }
        }
    }

    @Override
    public void createRoleRouter(Long idRole, List<Long> idRouter) throws Exception {
        Optional<Role> role = roleRepository.findById(idRole);
        if (role.isPresent()) {
            List<Router> routers = routerRepository.findAllRouterByListId(idRouter);
            for (Router router : routers) {
                RouterRole routerRole = new RouterRole();
                routerRole.setRouter(router);
                routerRole.setRole(role.get());
                routerRoleRepository.save(routerRole);
            }
        } else throw new Exception();
    }

}
