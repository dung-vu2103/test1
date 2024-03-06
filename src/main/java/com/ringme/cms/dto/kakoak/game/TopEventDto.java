package com.ringme.cms.dto.kakoak.game;

import lombok.Data;

@Data
public class TopEventDto {
    private String msisdn;
    private Long score;
    private String date;
    private String name;
    private String avatar;
    private Integer gender;
}
