package com.ringme.cms.dto.kakoakcms.customer;

import lombok.Data;

@Data
public class OfficialAccountDto {
    private Long id;
    private String officialAccountId;
    private String name;
    private String description;
    private String imagePath;
    private Integer status;
}
