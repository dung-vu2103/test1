package com.ringme.cms.service.kakoak.game;

import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.kakoak.game.GameCategoryDto;
import com.ringme.cms.model.kakoak.game.GameCategory;
import com.ringme.cms.repository.kakoak.game.GameCategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;

@Service
@Log4j2
@Transactional(value = "kakoakTransactionManager")
public class GameCategoryServiceImpl implements GameCategoryService {
    @Autowired
    GameCategoryRepository gameCategoryRepository;

    @Autowired
    UploadFile uploadFile;

    @Override
    public Page<GameCategory> getList(String gameCateName, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if (gameCateName != null) {
            if (gameCateName.trim().equals("")) {
                gameCateName = null;
            } else {
                gameCateName = gameCateName.trim().replaceAll("\s+", " ");
            }
        }

        return gameCategoryRepository.getList(gameCateName, pageable);
    }

    @Override
    public GameCategory findById(Long id) {
        try {
            GameCategory gameCategory = gameCategoryRepository.findById(id).orElse(null);
            return gameCategory;
        }
        catch (Exception e){
            log.info("Exception",e);
        }
        return null;
    }

    @Override
    public void save(GameCategory object) {
        gameCategoryRepository.save(object);
    }

    @Override
    public void saveGameCate(GameCategoryDto dto, String statusForm, String thumbUpload) {
        try {
            Long gameCateId = dto.getId();
            GameCategory object;
            //Check create or update
            if (gameCateId == null) {
                log.info("--------Icon Prize này đã tồn tại!------------");
                object = new GameCategory();
            } else {
                object = findById(dto.getId());
            }
            object.setName(dto.getName());
            object.setOrder(dto.getOrder());
            object.setStatus(dto.getStatus());
            //Save images to server
            Path fileName = uploadFile.createImageFile(thumbUpload, "image");
            if (gameCateId == null || fileName != null) {
                object.setIcon(File.separator + fileName);
            }
            log.info("SAVE OBJECT|" + object);
            save(object);
        } catch (Exception e) {
            log.error("ERROR SAVE|" + e.getMessage(), e);
        }
    }

    @Override
    public void deletegameCate(Long id) {
        try {
            gameCategoryRepository.deletegameCate(id);
        } catch (Exception e) {
            log.error("----------ERROR DELETE ICON|----------" + e.getMessage(), e);
            throw e;
        }
    }
}
