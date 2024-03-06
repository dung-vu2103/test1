package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.Router;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RouterService {
    void addRouter(Router router) throws Exception;

    List<Router> findAllRouterUnActive();

    List<Router> findAllRouterActive();

    void updateStatus(boolean check, Long id);

    List<Router> findAllRouterNotInRole(List<Long> roleIds);

    Optional<Router> findRouterById(Long id);
    Page<Router> page(int pageNo, int pageSize);
    void delete(Long id);
}
