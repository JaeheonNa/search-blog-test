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
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map defaultException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        Map<String, Object> response = new HashMap<>();
        response.put("ExceptionName", e.getClass().getName());
        response.put("ExceptionMessage", e.getMessage());
        return response;
    }

    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map resourceAccessException(Exception e){
        log.error(ExceptionUtils.getStackTrace(e));
        Map<String, Object> response = new HashMap<>();
        response.put("ExceptionName", e.getClass().getName());
        response.put("ExceptionMessage", "현재 검색 서비스가 원할하지 않습니다. 잠시 후 다시 시도해주세요.");
        return response;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map missingServletRequestParameterException(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        Map<String, Object> response = new HashMap<>();
        response.put("ExceptionName", e.getClass().getName());
        response.put("ExceptionMessage", "요청값이 올바르지 않습니다. 요청값을 올바르게 써주세요.");
        return response;
    }
}
