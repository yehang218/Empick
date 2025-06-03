package com.piveguyz.empickbackend.common.exception;

import com.piveguyz.empickbackend.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ResponseCode code;

    public BusinessException(ResponseCode code) {
        super(code.getMessage());
        this.code = code;
    }
}
