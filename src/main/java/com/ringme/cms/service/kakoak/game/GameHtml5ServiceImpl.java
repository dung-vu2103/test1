package com.ringme.cms.service.kakoak.game;

import com.ringme.cms.common.Helper;
import com.ringme.cms.common.UploadFile;
import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.dto.kakoak.game.GameCategoryDto;
import com.ringme.cms.dto.kakoak.game.GameHtml5Dto;
import com.ringme.cms.model.kakoak.game.GameCategory;
import com.ringme.cms.model.kakoak.game.GameHtml5;
import com.ringme.cms.repository.kakoak.game.GameCategoryRepository;
import com.ringme.cms.repository.kakoak.game.GameHtml5Repository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
@Log4j2
@Transactional(value = "kakoakTransactionManager")
public class GameHtml5ServiceImpl implements GameHtml5Service {
    @Autowired
    GameHtml5Repository gameHtml5Repository;

    @Autowired
    UploadFile uploadFile;

    @Override
    public Page<GameHtml5> getList(String gameHtml5Name, Integer visible, String font, Integer order, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        if (gameHtml5Name != null) {
            if (gameHtml5Name.trim().equals("")) {
                gameHtml5Name = null;
            } else {
                gameHtml5Name = gameHtml5Name.trim().replaceAll("\s+", " ");
            }
        }

        if (font != null) {
            if (font.trim().equals("")) {
                font = null;
            } else {
                font = font.trim().replaceAll("\s+", " ");
            }
        }

        return gameHtml5Repository.getList(gameHtml5Name, visible, font, order, pageable);
    }

    @Override
    public GameHtml5 findById(Long id) {
        try {
            GameHtml5 gameHtml5 = gameHtml5Repository.findById(id).orElse(null);
            return gameHtml5;
        }
        catch (Exception e){
            log.info("Exception",e);
        }
        return null;
    }

    @Override
    public void save(GameHtml5 object) {
        gameHtml5Repository.save(object);
    }

    @Override
    public void saveGameHtml5(GameHtml5Dto dto, String statusForm, String thumbUpload, String thumbUpload2, MultipartFile file) {
        try {
            Long gameHtml5Id = dto.getId();
            GameHtml5 object;
            //Check create or update
            if (gameHtml5Id == null) {
                log.info("--------Icon Prize này đã tồn tại!--------");
                object = new GameHtml5();
            } else {
                object = findById(dto.getId());
            }
            object.setName(dto.getName());
            object.setLink(dto.getLink());
            object.setVisible(dto.getVisible());
            object.setRecommend(dto.getRecommend());
            object.setOrder(dto.getOrder());
            object.setDescription(dto.getDescription());

            //Save images to server
            Path fileNameBanner = null;
            if (thumbUpload != null) {
                fileNameBanner = uploadFile.createImageFile(thumbUpload, "image");
                if (gameHtml5Id == null || fileNameBanner != null) {
                    object.setBanner(File.separator + fileNameBanner);
                }
            } else {
                object.setBanner(null);
            }

            Path fileNameIcon = null;
            if (thumbUpload2 != null) {
                fileNameIcon = uploadFile.createImageFile(thumbUpload2, "image");
                if (gameHtml5Id == null || fileNameIcon != null) {
                    object.setIcon(File.separator + fileNameIcon);
                }
            } else {
                object.setIcon(null);
            }

            Path fileNameFont = null;
            if (file != null) {
                fileNameFont = uploadFile.createGameFile(file, "font");
                if (gameHtml5Id == null || fileNameFont != null) {
                    object.setFont(File.separator + fileNameFont);
                }
            } else {
                object.setFont(null);
            }

            log.info("SAVE OBJECT|" + object);
            save(object);
        } catch (Exception e) {
            log.error("ERROR SAVE|" + e.getMessage(), e);
        }
    }

    @Override
    public void deleteGameHtml5(Long id) {
        try {
            gameHtml5Repository.deleteGameHtml5(id);
        } catch (Exception e) {
            log.error("----------ERROR DELETE ICON|----------" + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String getGameName(Long id) {
        return gameHtml5Repository.getGameName(id);
    }

    @Override
    public List<AjaxSearchDto> ajaxSearchGameId(String input) {
        return Helper.listAjax(gameHtml5Repository.ajaxSearchGameId(Helper.processStringSearch(input)), 1);
    }
}
