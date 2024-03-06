package com.ringme.cms.exception;

import com.ringme.cms.dto.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Log4j2
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(
            value = {IllegalArgumentException.class,
                    InsufficientAuthenticationException.class,
                    IOException.class,
                    RuntimeException.class})
    public ResponseEntity<?> handleExceptionA(Exception e, HttpServletRequest request) {
        log.error(e.getMessage() + " " + request.getRequestURI() + " User-Agent: " + request.getHeader("User-Agent"), e);
        Response response = Response.builder()
                .code(401)
                .message(e.getMessage())
                .data("")
                .build();
        return ResponseEntity.status(401).body(response);
    }

    @ExceptionHandler(
            value = {NotAuthenException.class})
    public ResponseEntity<?> handleExceptionB(Exception e) {
        log.error(e.getMessage(), e);
        Response response = Response.builder()
                .code(403)
                .message(e.getMessage())
                .data("")
                .build();
        return ResponseEntity.status(403).body(response);
    }

    @ExceptionHandler(value = {MissingRequestHeaderException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<?> handleMissParamException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage()+ "URL: " + request.getRequestURI() + " User-Agent: " + request.getHeader("User-Agent"));
        Response response = Response.builder()
                .code(500)
                .message("failed")
                .data("")
                .build();
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnwantedException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage()+ "URL: " + request.getRequestURI() + " User-Agent: " + request.getHeader("User-Agent"), e);
        Response response = Response.builder()
                .code(500)
                .message("failed")
                .data("")
                .build();
        return ResponseEntity.status(500).body(response);
    }
}
