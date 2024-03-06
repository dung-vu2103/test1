package com.ringme.cms.service.kakoak.game;

import com.ringme.cms.model.kakoak.game.GameStat;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;

public interface GameStatService {
    Page<GameStat> getList(Long gameId, String date, int pageNo, int pageSize);

    void exportExcel(Long gameId, String date, HttpServletResponse response);
}
