package com.piveguyz.empickbackend.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  ApiResponse 기반 커스텀 예외 핸들러
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.valueOf(ex.getCode().getCode()))
                .body(CustomApiResponse.of(ex.getCode()));
    }

    /**
     * @Valid 유효성 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomApiResponse<String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(CustomApiResponse.of(ResponseCode.VALIDATION_FAIL, errorMessage));
    }

    /**
     * @Validated 유효성 검증 실패 처리 (예: @RequestParam)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomApiResponse<String>> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.badRequest()
                .body(CustomApiResponse.of(ResponseCode.VALIDATION_FAIL, ex.getMessage()));
    }

    /**
     * 그 외 모든 예외 처리 (예기치 않은 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomApiResponse<Void>> handleException(Exception ex) {
        ex.printStackTrace(); // 로그로 남기기
        return ResponseEntity.internalServerError()
                .body(CustomApiResponse.of(ResponseCode.INTERNAL_SERVER_ERROR));
    }
}
