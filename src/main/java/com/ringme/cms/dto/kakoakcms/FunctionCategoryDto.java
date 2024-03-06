package com.ringme.cms.dto.kakoakcms;

import lombok.Data;

import java.io.Serializable;

@Data
public class FunctionCategoryDto implements Serializable {
    private Long id;
    private String icon;
    private String title;
    private Integer type;
    private Integer enable;
    private String deeplink;
    private Integer priority;
}