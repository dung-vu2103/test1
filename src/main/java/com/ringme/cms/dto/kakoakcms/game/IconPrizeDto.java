package com.ringme.cms.dto.kakoakcms.game;

import lombok.Data;

@Data
public class IconPrizeDto {
    private Long id;
    private String code;
    private String name;
    private String type;
    private String link_icon;
    private boolean active;
    private Long value;
}
