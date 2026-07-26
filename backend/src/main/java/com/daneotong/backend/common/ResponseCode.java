package com.daneotong.backend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ResponseCode {
    SUCCESS("SUCCESS", "요청이 성공했습니다.", HttpStatus.OK),
    UNKNOWN_ERROR("FAIL", "알 수 없는 이유로 응답에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ResponseCode(String code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
