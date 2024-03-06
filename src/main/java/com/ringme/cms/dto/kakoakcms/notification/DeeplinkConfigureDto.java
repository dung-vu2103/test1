package com.ringme.cms.dto.kakoakcms.notification;

import lombok.Data;

@Data
public class DeeplinkConfigureDto {
    private Long id;
    private String title;
    private String deeplink;
    private Integer isActive;
    private String description;
    private String deeplinkParams;
    private Integer type;

}
