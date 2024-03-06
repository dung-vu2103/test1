package com.ringme.cms.dto.kakoakcms.sticker;

import lombok.Data;

@Data
public class StickerDto {
    private Long id;
    private String name;
    private Integer iorder;
    private String prefix;
    private Integer itemTotal;
    private String stickerUrl;
    private String iconUrl;
    private String previewUrl;
    private Integer active;
    private Integer isNew;
    private Integer sticky;
    private String createdDate;
    private String modified;
    private String modifiedItemDate;
}
