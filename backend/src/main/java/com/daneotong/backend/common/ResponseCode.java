package com.daneotong.backend.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("SUCCESS", "요청이 성공했습니다.");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
