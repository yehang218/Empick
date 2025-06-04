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
    MEMBER_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1000, "사원 정보를 찾을 수 없습니다."),
    MEMBER_RESIGNED(false, HttpStatus.NOT_ACCEPTABLE,1001, "삭제된 사원 정보입니다."),
    MEMBER_STATUS_SUSPENDED(false, HttpStatus.NOT_ACCEPTABLE, 1004, "사원 상태가 차단되었습니다."),


    // 채용 오류 - 2000 ~ 2999
    // 채용 공고 - 2000 ~ 2099

    // 3) 채용공고
    EMPLOYMENT_RECRUITMENT_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2030, "요청한 채용 공고를 찾을 수 없습니다."),
    EMPLOYMENT_RECRUITMENT_INVALID_PERIOD(false, HttpStatus.BAD_REQUEST, 2031, "채용 기간이 유효하지 않습니다."),
    EMPLOYMENT_RECRUITMENT_ALREADY_DELETED(false, HttpStatus.GONE, 2032, "이미 삭제된 채용 공고입니다."),
    EMPLOYMENT_RECRUITMENT_UNAUTHORIZED_ACCESS(false, HttpStatus.FORBIDDEN, 2033, "채용 공고에 접근할 권한이 없습니다."),
    EMPLOYMENT_RECRUITMENT_CANNOT_MODIFY_PUBLISHED(false, HttpStatus.CONFLICT, 2034, "게시된 채용 공고는 수정할 수 없습니다."),
    EMPLOYMENT_RECRUITMENT_NO_TITLE(false, HttpStatus.BAD_REQUEST, 2035, "채용 공고 제목을 입력하지 않았습니다."),
    EMPLOYMENT_RECRUITMENT_NO_CONTENT(false, HttpStatus.BAD_REQUEST, 2036, "채용 공고 내용을 입력하지 않았습니다."),
    EMPLOYMENT_RECRUITMENT_NO_START_DATE(false, HttpStatus.BAD_REQUEST, 2037, "채용 시작일을 입력하지 않았습니다."),
    EMPLOYMENT_RECRUITMENT_NO_END_DATE(false, HttpStatus.BAD_REQUEST, 2038, "채용 마감일을 입력하지 않았습니다."),
    EMPLOYMENT_RECRUITMENT_INVALID_DATE_ORDER(false, HttpStatus.BAD_REQUEST, 2039, "마감일은 시작일보다 이후여야 합니다."),
    EMPLOYMENT_RECRUITMENT_NO_INTRODUCE_TEMPLATE(false, HttpStatus.BAD_REQUEST, 2040, "자기소개서 템플릿이 지정되지 않았습니다."),
    EMPLOYMENT_RECRUITMENT_NO_MEMBER_ID(false, HttpStatus.BAD_REQUEST, 2041, "작성자 정보가 누락되었습니다."),
    EMPLOYMENT_RECRUITMENT_DUPLICATE_TITLE(false, HttpStatus.CONFLICT, 2042, "같은 제목의 채용 공고가 이미 존재합니다."),
    //  지원자 - 2100 ~ 2199


    //  지원서 - 2200 ~ 2299


    //  자기소개서 - 2300 ~ 2399


    //  실무테스트 - 2400 ~ 2499
    EMPLOYMENT_INVALID_DIFFICULTY(false, HttpStatus.INTERNAL_SERVER_ERROR, 2400, "유효하지 않은 난이도입니다."),
    EMPLOYMENT_INVALID_TYPE(false, HttpStatus.INTERNAL_SERVER_ERROR, 2401, "유효하지 않은 실무 테스트 유형입니다."),

    //   1) 실무테스트 문제
    EMPLOYMENT_QUESTION_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, 2410, "실무테스트 문제 등록에 실패했습니다."),
    EMPLOYMENT_QUESTION_DUPLICATE(false, HttpStatus.CONFLICT, 2411, "동일한 문제가 이미 등록되어 있습니다."),
    EMPLOYMENT_QUESTION_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2412, "요청한 문제를 찾을 수 없습니다."),
    EMPLOYMENT_QUESTION_INVALID_TYPE(false, HttpStatus.BAD_REQUEST, 2413, "유효하지 않은 실무 테스트 유형입니다."),
    EMPLOYMENT_QUESTION_INVALID_DIFFICULTY(false, HttpStatus.BAD_REQUEST, 2414, "유효하지 않은 난이도입니다."),
    EMPLOYMENT_QUESTION_INVALID_MEMBER(false, HttpStatus.BAD_REQUEST, 2415, "작성자 정보가 유효하지 않습니다."),

    //  면접 일정 - 2500 ~ 2599


    //  안내 메일 - 2600 ~ 2699
    EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE(false, HttpStatus.CONFLICT, 2600, "이름이 중복된 템플릿이 존재합니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE(false, HttpStatus.BAD_REQUEST, 2601, "제목을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT(false, HttpStatus.BAD_REQUEST, 2602, "내용을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2603, "존재하지 않는 템플릿입니다."),

    EMPLOYMENT_MAIL_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2630, "요청한 메일을 찾을 수 없습니다."),
    EMPLOYMENT_MAIL_NO_CONTENT(false, HttpStatus.BAD_REQUEST, 2631, "메일의 내용을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_INADEQUATE_EMAIL(false, HttpStatus.CONFLICT, 2632, "유효하지 않은 형태의 이메일입니다.");



    private final boolean success;
    private final HttpStatus httpStatus;        // HTTP 상태 코드
    private final int code;                     // 비지니스 로직
    private final String message;
}
