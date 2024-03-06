package com.ringme.cms.dto.kakoak.game;

import lombok.*;

@Data
public class GameCategoryDto {
    private Long id;
    private String name;
    private Integer order;
    private String icon;
    private Integer status;
}
