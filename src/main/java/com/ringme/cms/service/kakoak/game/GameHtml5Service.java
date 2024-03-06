package com.ringme.cms.service.kakoak.game;

import com.ringme.cms.dto.AjaxSearchDto;
import com.ringme.cms.dto.kakoak.game.GameHtml5Dto;
import com.ringme.cms.model.kakoak.game.GameHtml5;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GameHtml5Service {
    Page<GameHtml5> getList(String gameHtml5Name, Integer visible, String font, Integer order, int pageNo, int pageSize);

    GameHtml5 findById(Long id);

    void save(GameHtml5 object);

    void saveGameHtml5(GameHtml5Dto dto, String statusForm, String thumbUpload, String thumbUpload2, MultipartFile file);

    void deleteGameHtml5(Long id);

    String getGameName(Long id);

    List<AjaxSearchDto> ajaxSearchGameId(String input);
}
