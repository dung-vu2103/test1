package com.ringme.cms.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Response {
    private int code;
    private String message;
    private Object data;
}
