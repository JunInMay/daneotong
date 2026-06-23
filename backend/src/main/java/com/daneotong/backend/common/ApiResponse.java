package com.daneotong.backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> of(ResponseCode code, T data) {
        return new ApiResponse<>(code.getCode(), code.getMessage(), data);
    }

    public static <T> ApiResponse<T> of(ResponseCode code, String message, T data) {
        return new ApiResponse<>(code.getCode(), message, data);
    }
}
