package com.ringme.cms.service.sys;


import com.ringme.cms.model.sys.Menu;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface MenuService {

    void saveMenu(Menu menu);

    Optional<Menu> findMenuById(Long id);

    void deleteById(Long id);

    Page<Menu> findMenuPage(int pageNo, int pageSize);

    List<Menu> getListMenuNoParent();

//    Map<String, List<Menu>> getMapMenuParent();

    Optional<Menu> findMenuByName(String name);
}
