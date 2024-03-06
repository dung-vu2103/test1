package com.ringme.cms.dto;

import lombok.*;

@Data
public class AjaxSearchDto {
    private String id;
    private String text;
    private String deeplink;
    private String deeplinkParams;
}
