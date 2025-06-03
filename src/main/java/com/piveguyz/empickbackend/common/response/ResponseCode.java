package com.piveguyz.empickbackend.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 성공/에러 메세지 코드 관리
 */

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // 모든 성공 코드
    SUCCESS(true, HttpStatus.OK, 200, "요청이 성공적으로 처리되었습니다."),

    // 클라이언트 오류 (4xx)
    BAD_REQUEST(false, HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
    FORBIDDEN(false, HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    NOT_FOUND(false, HttpStatus.NOT_FOUND, 404, "요청한 리소스를 찾을 수 없습니다."),
    VALIDATION_FAIL(false, HttpStatus.BAD_REQUEST, 405, "유효성 검증에 실패했습니다."),

    // 서버 오류 (5xx)
    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류입니다."),

    // 인사 오류 - 1000 ~ 1999


    // 채용 오류 -  2000 ~ 2999
    //  채용 공고 - 2000 ~ 2099


    //  지원자 - 2100 ~ 2199


    //  지원서 - 2200 ~ 2299


    //  자기소개서 - 2300 ~ 2399


    //  실무테스트 - 2400 ~ 2499
    EMPLOYMENT_INVALID_DIFFICULTY(false, HttpStatus.INTERNAL_SERVER_ERROR, 2400, "유효하지 않은 난이도입니다."),
    EMPLOYMENT_INVALID_TYPE(false, HttpStatus.INTERNAL_SERVER_ERROR, 2401, "유효하지 않은 실무 테스트 유형입니다."),

    //   1) 실무테스트 문제
    EMPLOYMENT_QUESTION_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, 2410, "실무테스트 문제 등록에 실패했습니다."),
    EMPLOYMENT_QUESTION_DUPLICATE(false, HttpStatus.CONFLICT, 2411, "동일한 문제가 이미 등록되어 있습니다."),
    EMPLOYMENT_QUESTION_INVALID_MEMBER(false, HttpStatus.BAD_REQUEST, 2412, "작성자 정보가 유효하지 않습니다."),
    EMPLOYMENT_QUESTION_INVALID_UPDATED_MEMBER(false, HttpStatus.BAD_REQUEST, 2413, "수정자 정보가 유효하지 않습니다."),
    EMPLOYMENT_QUESTION_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2414, "요청한 문제를 찾을 수 없습니다.");

    //  면접 일정 - 2500 ~ 2599


    //  안내 메일 - 2600 ~ 2699


    private final boolean success;
    private final HttpStatus httpStatus;        // HTTP 상태 코드
    private final int code;                     // 비지니스 로직
    private final String message;
}
