package com.ringme.cms.service.kakoak.game;

import com.ringme.cms.dto.kakoak.game.GameCategoryDto;
import com.ringme.cms.model.kakoak.game.GameCategory;
import org.springframework.data.domain.Page;

public interface GameCategoryService {
    Page<GameCategory> getList(String gameCateName, int pageNo, int pageSize);

    GameCategory findById(Long id);

    void save(GameCategory object);

    void saveGameCate(GameCategoryDto dto, String statusForm, String thumbUpload);

    void deletegameCate(Long id);
}
