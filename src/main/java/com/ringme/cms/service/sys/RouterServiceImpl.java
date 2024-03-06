package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.Router;
import com.ringme.cms.repository.sys.RouterRepository;
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
public class RouterServiceImpl implements RouterService {
    @Autowired
    RouterRepository routerRepository;

    @Override
    public void addRouter(Router router) throws Exception {
        try {
            routerRepository.save(router);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public List<Router> findAllRouterUnActive() {
        return routerRepository.findAllRouterUnActive();
    }

    @Override
    public List<Router> findAllRouterActive() {
        return routerRepository.findAllRouterActive();
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void updateStatus(boolean check, Long id) {
        routerRepository.updateStatus(check, id);
    }

    @Override
    public List<Router> findAllRouterNotInRole(List<Long> roleIds) {
        return routerRepository.findAllRouterNotInRole(roleIds);
    }

    @Override
    public Optional<Router> findRouterById(Long id) {
        return routerRepository.findById(id);
    }
    @Override
    public Page<Router> page(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return routerRepository.search(pageable);
    }

    @Override
    public void delete(Long id) {
        routerRepository.deleteById(id);
    }
}
