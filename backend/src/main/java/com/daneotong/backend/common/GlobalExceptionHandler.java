package com.daneotong.backend.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<ApiResponse<Object>> error(DaneotongException e) {
        // TODO : logging 작업 필요
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(ApiResponse.of(e));
    }

    @ExceptionHandler
    ResponseEntity<ApiResponse<Object>> unexpectedError(Exception e) {
        // TODO : logging 작업 필요
        ResponseCode code = ResponseCode.UNKNOWN_ERROR;

        return ResponseEntity.status(code.getHttpStatusCode())
                .body(ApiResponse.of(code));
    }
}
