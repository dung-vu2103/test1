package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.RouterRole;

import java.util.List;

public interface RoleRouterService {
    List<RouterRole> findAllRouterRole();

    List<RouterRole> findAllRouterRoleByRoleId(Long roleId);

    List<RouterRole> findAllRouterRoleByListRoleId(List<Long> roleIds);

    void saveRoleRouter(RouterRole routerRole);

    void deleteRoleRouter(List<Long> id) throws Exception;

    void createRoleRouter(Long idRole, List<Long> idRouter) throws Exception;

}
