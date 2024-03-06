package com.ringme.cms.dto.kakoak.game;

import com.ringme.cms.model.kakoak.game.GameHtml5;
import lombok.*;

import java.util.Date;

@Data
public class GameStatDto {
    private Integer No;
    private Integer gameId;
    private String gameName;
    private Integer getScore;
    private Integer submit;
    private Integer userNumber;
    private Date date;
}
