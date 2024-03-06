package com.ringme.cms.dto.kakoakcms.sticker;

import lombok.Data;

@Data
public class StickerItemDto {
    private Long id;
    private Long stickerId;
    private Integer iorder;
    private String imageUrl;
    private String voiceUrl;
    private Integer active;
    private String createdDate;
    private String modifiedDate;
}
