package com.kakaobank.searchblog.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected Map defaultException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        Map<String, Object> response = new HashMap<>();
        response.put("ExceptionName", e.getClass().getName());
        response.put("ExceptionMessage", e.getMessage());
        return response;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected Map missingServletRequestParameterException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        Map<String, Object> response = new HashMap<>();
        response.put("ExceptionName", e.getClass().getName());
        response.put("ExceptionMessage", e.getMessage());
        return response;
    }
}
