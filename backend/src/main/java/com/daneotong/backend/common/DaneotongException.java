package com.daneotong.backend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class DaneotongException extends RuntimeException{

    private final ResponseCode responseCode;

    public DaneotongException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public DaneotongException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public HttpStatusCode getHttpStatusCode() {
        return getResponseCode().getHttpStatusCode();
    }
}
