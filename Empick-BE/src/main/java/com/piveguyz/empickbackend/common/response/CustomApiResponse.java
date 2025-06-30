package com.piveguyz.empickbackend.common.response;

import lombok.Getter;
import lombok.Setter;

/**
 * API 응답 공통 클래스
 * 사용 필드
 *  - success : 성공 여부(true / false)
 *  - code : HTTP 상태 코드(ResponseCode.java 참고)
 *  - message : 응답 메세지
 *  - data : 응답 데이터
 */

@Getter
@Setter
public class CustomApiResponse<T> {
    private boolean success;
    private int code;
    private String message;
    private T data;

    public CustomApiResponse() {
    }

    public CustomApiResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CustomApiResponse<T> of(ResponseCode code, T data) {
        return new CustomApiResponse<>(code.isSuccess(), code.getCode(), code.getMessage(), data);
    }

    public static <T> CustomApiResponse<T> of(ResponseCode code) {
        return new CustomApiResponse<>(code.isSuccess(), code.getCode(), code.getMessage(), null);
    }
}
