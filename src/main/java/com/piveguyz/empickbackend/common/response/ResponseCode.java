package com.piveguyz.empickbackend.common.response;

/**
 * 성공/에러 메세지 코드 관리
 */

public enum ResponseCode {

    // 모든 성공 코드
    SUCCESS(true, 200, "요청이 성공적으로 처리되었습니다."),

    // 클라이언트 오류 (4xx)
    BAD_REQUEST(false, 400, "잘못된 요청입니다."),
    UNAUTHORIZED(false, 401, "인증이 필요합니다."),
    FORBIDDEN(false, 403, "접근 권한이 없습니다."),
    NOT_FOUND(false, 404, "요청한 리소스를 찾을 수 없습니다."),
    VALIDATION_FAIL(false, 405, "유효성 검증에 실패했습니다."),

    // 서버 오류 (5xx)
    INTERNAL_SERVER_ERROR(false, 500, "서버 내부 오류입니다."),

    // 인사 오류 - 1000 ~ 1999



    // 채용 오류 - 2000 ~ 2999
    // 채용 공고 - 2000 ~ 2099


    //  지원자 - 2100 ~ 2199


    //  지원서 - 2200 ~ 2299


    //  자기소개서 - 2300 ~ 2399


    //  실무테스트 - 2400 ~ 2499
    EMPLOYMENT_QUESTION_FAIL(false, 2400, "실무테스트 문제 등록에 실패했습니다."),
    EMPLOYMENT_QUESTION_DUPLICATE(false, 2401, "동일한 문제가 이미 등록되어 있습니다"),
    EMPLOYMENT_QUESTION_NOT_FOUND(false, 2402, "요청한 문제를 찾을 수 없습니다."),

    //  면접 일정 - 2500 ~ 2599


    //  안내 메일 - 2600 ~ 2699
    EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE(false, 2600, "이름이 중복된 템플릿이 존재합니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE(false, 2601, "제목을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT(false, 2602, "내용을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND(false, 2603, "존재하지 않는 템플릿입니다."),
    EMPLOYMENT_MAIL_INADEQUATE_EMAIL(false, 2604, "유효하지 않은 형태의 이메일입니다.");



    private final boolean success;
    private final int code;
    private final String message;

    ResponseCode(boolean sucecss, int code, String message) {
        this.success = sucecss;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
