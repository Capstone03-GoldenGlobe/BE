package com.capstone03.goldenglobe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseSetting<T> {
    private final int status;
    private final String message;
    private final T data;

    // 반환 데이터 X
    public ApiResponseSetting(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    // 반환 데이터 O
    public ApiResponseSetting(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
