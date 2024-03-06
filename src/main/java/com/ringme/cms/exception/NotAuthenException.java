package com.ringme.cms.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotAuthenException extends RuntimeException {
    private Integer code;
    private String message;
}
