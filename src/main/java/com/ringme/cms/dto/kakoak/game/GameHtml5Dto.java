package com.ringme.cms.dto.kakoak.game;

import lombok.Data;

@Data
public class GameHtml5Dto {
    private Long id;
    private String name;
    private String link;
    private String description;
    private Integer recommend;
    private String banner;
    private String icon;
    private Integer visible;
    private String order;
    private String font;
}
