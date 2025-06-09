package com.piveguyz.empickbackend.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

/**
 * 성공/에러 메세지 코드 관리
 */

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // 모든 성공 코드
    SUCCESS(true, HttpStatus.OK, 200, "요청이 성공적으로 처리되었습니다."),
    CREATED(true, HttpStatus.CREATED, 201, "리소스가 성공적으로 생성되었습니다."),

    // 클라이언트 오류 (4xx)
    BAD_REQUEST(false, HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED, 401, "인증이 필요합니다."),
    FORBIDDEN(false, HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    NOT_FOUND(false, HttpStatus.NOT_FOUND, 404, "요청한 리소스를 찾을 수 없습니다."),
    VALIDATION_FAIL(false, HttpStatus.BAD_REQUEST, 405, "유효성 검증에 실패했습니다."),

    // 서버 오류 (5xx)
    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류입니다."),
    DATABASE_CONNECTION_ERROR(false, HttpStatus.SERVICE_UNAVAILABLE, 503, "DB 연결에 실패했습니다."),

    // 인사 오류 - 1000 ~ 1999
    MEMBER_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1000, "사원 정보를 찾을 수 없습니다."),
    MEMBER_RESIGNED(false, HttpStatus.NOT_ACCEPTABLE, 1001, "삭제된 사원 정보입니다."),
    MEMBER_STATUS_SUSPENDED(false, HttpStatus.NOT_ACCEPTABLE, 1004, "사원 상태가 차단되었습니다."),
    MEMBER_EMAIL_DUPLICATED(false, HttpStatus.NOT_ACCEPTABLE, 1005, "이미 존재하는 이메일입니다."),
    MEMBER_CREATED_MEMBER_ID_REQUIRED(false, HttpStatus.BAD_REQUEST, 1006, "입사처리자가 지정되지 않았습니다."),
    MEMBER_CREATED_MEMBER_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1007, "입사처리자를 찾을 수 없습니다."),
    MEMBER_CREATED_MEMBER_NO_PERMISSION(false, HttpStatus.FORBIDDEN, 1008, "입사처리자는 ROLE_HR_ACCESS 권한이 있어야 합니다."),
    NO_HR_PERMISSION(false, HttpStatus.FORBIDDEN, 1008, "ROLE_HR_ACCESS 권한이 있어야 합니다."),

    MEMBER_PROFILE_IMAGE_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1009 , "사원의 프로필 사진을 찾을 수 없습니다." ),
    MEMBER_ID_INVALID(false, HttpStatus.BAD_REQUEST, 1010, "유효하지 않은 사원 ID 입니다."),
    DUPLICATE_EDIT_REQUEST(false, HttpStatus.CONFLICT, 1011, "같은 필드에 대해 이미 대기중인 수정 요청이 존재합니다."),
    EDIT_PROPOSAL_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1100, "변경 요청을 찾을 수 없습니다."),
    EDIT_PROPOSAL_ALREADY_REJECTED(false, HttpStatus.BAD_REQUEST, 1101, "이미 거절된 요청입니다. 변경할 수 없습니다."),
    EDIT_PROPOSAL_SELF_APPROVE_NOT_ALLOWED(false, HttpStatus.FORBIDDEN, 1102, "본인이 요청한 변경은 본인이 처리할 수 없습니다."),
    EDIT_PROPOSAL_INVALID_FIELD(false, HttpStatus.BAD_REQUEST, 1103, "유효하지 않은 필드입니다."),
    EDIT_PROPOSAL_APPROVED(true, HttpStatus.OK, 1104, "변경 요청이 승인되었습니다."),
    EDIT_PROPOSAL_REJECTED(true, HttpStatus.OK, 1105, "변경 요청이 거절되었습니다."),

    // 부서, 직책, 직무, 직급 관련 NOT_FOUND
    DEPARTMENT_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1200, "요청한 부서를 찾을 수 없습니다."),
    POSITION_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1201, "요청한 직책을 찾을 수 없습니다."),
    JOB_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1202, "요청한 직무를 찾을 수 없습니다."),
    RANK_NOT_FOUND(false, HttpStatus.NOT_FOUND, 1203, "요청한 직급을 찾을 수 없습니다."),

    // 채용 오류 - 2000 ~ 2999
    // 채용 공고 - 2000 ~ 2099
    // 1) 채용 요청서
    EMPLOYMENT_REQUEST_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2000, "채용 요청서를 찾을 수 없습니다."),
    EMPLOYMENT_REQUEST_MISSING_DEPARTMENT(false, HttpStatus.BAD_REQUEST, 2001, "부서를 선택하지 않았습니다."),
    EMPLOYMENT_REQUEST_INVALID_HEADCOUNT(false, HttpStatus.BAD_REQUEST, 2002, "모집 인원은 1명 이상이어야 합니다."),
    EMPLOYMENT_REQUEST_INVALID_DATE_RANGE(false, HttpStatus.BAD_REQUEST, 2003, "시작일은 마감일보다 이전이어야 합니다."),
    EMPLOYMENT_REQUEST_MISSING_EMPLOYMENT_TYPE(false, HttpStatus.BAD_REQUEST, 2004, "고용 형태를 입력해야 합니다."),
    EMPLOYMENT_REQUEST_MISSING_QUALIFICATION(false, HttpStatus.BAD_REQUEST, 2005, "자격 요건을 입력해야 합니다."),
    EMPLOYMENT_REQUEST_MISSING_RESPONSIBILITY(false, HttpStatus.BAD_REQUEST, 2006, "담당 업무를 입력해야 합니다."),
    EMPLOYMENT_REQUEST_ALREADY_EXISTS(false, HttpStatus.CONFLICT, 2007, "해당 기간 내 중복된 채용 요청이 존재합니다."),

    // 2) 채용 템플릿
    EMPLOYMENT_TEMPLATE_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2010, "요청한 템플릿을 찾을 수 없습니다."),
    EMPLOYMENT_TEMPLATE_NO_NAME(false, HttpStatus.BAD_REQUEST, 2011, "템플릿 이름을 입력하지 않았습니다."),
    EMPLOYMENT_TEMPLATE_ALREADY_DELETED(false, HttpStatus.GONE, 2012, "이미 삭제된 템플릿입니다."),
    EMPLOYMENT_TEMPLATE_UNAUTHORIZED_ACCESS(false, HttpStatus.FORBIDDEN, 2013, "해당 템플릿에 대한 접근 권한이 없습니다."),
    EMPLOYMENT_TEMPLATE_NO_ITEMS(false, HttpStatus.BAD_REQUEST, 2014, "템플릿 항목이 하나 이상 필요합니다."),
    EMPLOYMENT_TEMPLATE_DUPLICATE_NAME(false, HttpStatus.CONFLICT, 2015, "같은 이름의 템플릿이 이미 존재합니다."),

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
    EMPLOYMENT_RECRUITMENT_INVALID_STATUS_TRANSITION(false, HttpStatus.CONFLICT, 2043, "잘못된 상태 전환입니다. 승인 대기 → 승인 → 게시 → 종료 순으로만 변경 가능합니다."),

    // 4) 지원서 항목
    EMPLOYMENT_APPLICATION_ITEM_CATEGORY_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2050, "지원서 항목 카테고리를 찾을 수 없습니다."),
    EMPLOYMENT_APPLICATION_ITEM_CATEGORY_EMPTY(false, HttpStatus.NO_CONTENT, 2051, "지원서 항목 카테고리가 존재하지 않습니다."),
    EMPLOYMENT_APPLICATION_ITEM_DUPLICATED(false, HttpStatus.CONFLICT, 2052, "동일한 항목이 양식에 중복 포함되어 있습니다."),
    EMPLOYMENT_APPLICATION_ITEM_TEMPLATE_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2053, "해당 채용공고에 연결된 지원서 양식을 찾을 수 없습니다."),

    // 5) 채용 프로세스
    EMPLOYMENT_RECRUITMENT_PROCESS_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2060, "채용 프로세스를 찾을 수 없습니다."),
    EMPLOYMENT_RECRUITMENT_PROCESS_EMPTY(false, HttpStatus.NO_CONTENT, 2061, "채용 프로세스가 존재하지 않습니다."),
    EMPLOYMENT_RECRUITMENT_PROCESS_INVALID_STEP(false, HttpStatus.BAD_REQUEST, 2062, "유효하지 않은 전형 단계 코드입니다."),
    EMPLOYMENT_RECRUITMENT_PROCESS_DUPLICATED_ORDER(false, HttpStatus.CONFLICT, 2063, "채용 프로세스 순서(displayOrder)가 중복되었습니다."),
    EMPLOYMENT_RECRUITMENT_PROCESS_RECRUITMENT_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2064, "채용 공고를 찾을 수 없습니다."),

    // 6) 채용 공고 템플릿 복사본
    EMPLOYMENT_TEMPLATE_COPY_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2070, "채용 템플릿 복사본을 찾을 수 없습니다."),
    EMPLOYMENT_TEMPLATE_COPY_EMPTY(false, HttpStatus.BAD_REQUEST, 2071, "복사본이 비어 있습니다."),
    EMPLOYMENT_TEMPLATE_COPY_DUPLICATED_ORDER(false, HttpStatus.CONFLICT, 2072, "채용 템플릿 복사본 displayOrder가 중복되었습니다."),
    EMPLOYMENT_TEMPLATE_COPY_RECRUITMENT_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2073, "해당 채용공고에 대한 복사본이 없습니다."),

    //  지원자 - 2100 ~ 2199
    EMPLOYMENT_APPLICANT_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2100, "요청한 ID가 존재하지 않습니다."),

    //  지원서 - 2200 ~ 2299


    //  자기소개서 - 2300 ~ 2399


    //  실무테스트 - 2400 ~ 2499
    EMPLOYMENT_INVALID_DIFFICULTY(false, HttpStatus.INTERNAL_SERVER_ERROR, 2400, "유효하지 않은 난이도입니다."),
    EMPLOYMENT_INVALID_MEMBER(false, HttpStatus.BAD_REQUEST, 2402, "작성자 정보가 유효하지 않습니다."),
    EMPLOYMENT_INVALID_UPDATED_MEMBER(false, HttpStatus.BAD_REQUEST, 2403, "수정자 정보가 유효하지 않습니다."),
    EMPLOYMENT_INVALID_SATISFIED_VALUE(false, HttpStatus.BAD_REQUEST, 2404, "isSatisfied 값은 'Y' 또는 'N'이어야 합니다."),

    //   1) 실무테스트 문제
    EMPLOYMENT_QUESTION_FAIL(false, HttpStatus.INTERNAL_SERVER_ERROR, 2410, "실무테스트 문제 등록에 실패했습니다."),
    EMPLOYMENT_QUESTION_DUPLICATE(false, HttpStatus.CONFLICT, 2411, "동일한 문제가 이미 등록되어 있습니다."),
    EMPLOYMENT_INVALID_QUESTION_TYPE(false, HttpStatus.BAD_REQUEST, 2412, "유효하지 않은 실무테스트 문제 유형입니다."),
    EMPLOYMENT_QUESTION_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2414, "요청한 문제를 찾을 수 없습니다."),
    EMPLOYMENT_QUESTION_DELETE_CONFLICT(false, HttpStatus.CONFLICT, 2415, "이 문제는 다른 곳에서 사용 중이므로 삭제할 수 없습니다."),

    EMPLOYMENT_OPTION_COUNT_EXCEEDED(false, HttpStatus.BAD_REQUEST, 2416, "선택지는 최대 5개까지만 등록할 수 있습니다."),
    EMPLOYMENT_QUESTION_OPTION_NOT_FOUND(false, HttpStatus.NOT_FOUND, 2417, "선택지를 찾을 수 없습니다."),
    EMPLOYMENT_QUESTION_OPTION_MAX_NUMBER(false, HttpStatus.BAD_REQUEST, 2418, "선택지는 5번을 초과할 수 없습니다."),

    //   2) 실무테스트
    EMPLOYMENT_JOBTEST_DELETE_CONFLICT(false, HttpStatus.CONFLICT, 2420, "이 실무테스트는 다른 곳에서 사용중이므로 수정하거나 삭제할 수 없습니다."),
    EMPLOYMENT_JOBTEST_DUPLICATE(false, HttpStatus.CONFLICT, 2421, "동일한 이름의 실무테스트가 이미 등록되어 있습니다."),
    EMPLOYMENT_INVALID_JOBTEST(false, HttpStatus.NOT_FOUND, 2422, "요청한 실무테스트를 찾을 수 없습니다."),
    EMPLOYMENT_INVALID_JOBTEST_QUESTION(false, HttpStatus.NOT_FOUND, 2423, "이 실무테스트에는 해당 문제가 등록되어 있지 않습니다."),
    EMPLOYMENT_JOBTEST_QUESTION_DUPLICATE(false, HttpStatus.CONFLICT, 2424, "이 실무테스트에는 해당 문제가 이미 등록되어 있습니다."),

    //   3) 평가 기준
    EMPLOYMENT_INVALID_EVALUATION_CRITERIA(false, HttpStatus.BAD_REQUEST, 2430, "평가 기준이 유효하지 않습니다."),
    EMPLOYMENT_JOBTEST_EVALUATION_CRITERIA_OVER_WEIGHT(false, HttpStatus.BAD_REQUEST, 2431, "평가 기준의 가중치는 0 ~ 1을 벗어날 수 없습니다."),


    //   4) 채점 기준
    EMPLOYMENT_INVALID_QUESTION_GRADING_CRITERIA(false, HttpStatus.NOT_FOUND, 2440, "요청한 실무테스트 문제 평가 기준을 찾을 수 없습니다."),

    //   5) 실무테스트 답안
    EMPLOYMENT_INVALID_JOBTEST_ANSWER(false, HttpStatus.NOT_FOUND,2450, "요청한 답안을 찾을 수 없습니다."),

    // 4) 평가 결과
    EMPLOYMENT_INVALID_EVALUATION_RESULT(false, HttpStatus.NOT_FOUND, 2440, "요청한 평가 결과를 찾을 수 없습니다."),

    EMPLOYMENT_INVALID_CORRECTED_VALUE(false, HttpStatus.BAD_REQUEST, 2451, "isCorrect 값은 'Y' 또는 'N'이어야 합니다."),


    

    //  면접 일정 - 2500 ~ 2599
    EMPLOYMENT_INTERVIEW_CRITERIA_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2500, "존재하지 않는 면접 기준입니다."),
    EMPLOYMENT_INTERVIEW_CRITERIA_NO_CONTENT(false, HttpStatus.BAD_REQUEST, 2510, "내용을 입력하지 않았습니다."),
    EMPLOYMENT_INTERVIEW_CRITERIA_NO_DETAIL_CONTENT(false, HttpStatus.BAD_REQUEST, 2511, "상세 내용을 입력하지 않았습니다."),
    EMPLOYMENT_INTERVIEW_CRITERIA_DUPLICATE_CONTENT(false, HttpStatus.BAD_REQUEST, 2512, "이미 존재하는 내용입니다."),
    EMPLOYMENT_INTERVIEW_SHEET_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2520, "존재하지 않습니다."),
    EMPLOYMENT_INTERVIEW_SHEET_NO_NAME(false, HttpStatus.BAD_REQUEST, 2521, "이름을 입력하지 않았습니다."),
    EMPLOYMENT_INTERVIEW_SHEET_DUPLICATE_NAME(false, HttpStatus.CONFLICT, 2522, "중복된 이름이 존재합니다."),
    EMPLOYMENT_INTERVIEW_SHEET_ITEM_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2530, "존재하지 않는 항목입니다."),
    EMPLOYMENT_INTERVIEW_SHEET_ITEM_DUPLICATE(false, HttpStatus.BAD_REQUEST, 2531, "이미 존재하는 항목입니다."),
    EMPLOYMENT_INTERVIEW_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2540, "존재하지 않습니다."),
    EMPLOYMENT_INTERVIEW_NO_SHEET(false, HttpStatus.BAD_REQUEST, 2541, "평가표가 등록되지 않았습니다."),
    EMPLOYMENT_INTERVIEW_NO_DATE(false, HttpStatus.BAD_REQUEST, 2542, "날짜가 등록되지 않았습니다."),
    EMPLOYMENT_INTERVIEW_NO_ADDRESS(false, HttpStatus.BAD_REQUEST, 2543, "주소가 등록되지 않았습니다."),
    EMPLOYMENT_INTERVIEW_IMPOSSIBLE_TIME(false, HttpStatus.BAD_REQUEST, 2544, "불가능한 시간입니다."),
    EMPLOYMENT_INTERVIEW_SCORE_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2550, "존재하지 않습니다."),
    EMPLOYMENT_INTERVIEW_SCORE_NO_SCORE(false, HttpStatus.BAD_REQUEST, 2551, "점수를 입력하지 않았습니다."),
    EMPLOYMENT_INTERVIEW_SCORE_NO_REVIEW(false, HttpStatus.BAD_REQUEST, 2552, "평가를 입력하지 않았습니다."),
    EMPLOYMENT_INTERVIEW_SCORE_NO_ITEM(false, HttpStatus.BAD_REQUEST, 2553, "해당하는 평가 항목이 존재하지 않습니다."),
    EMPLOYMENT_INTERVIEW_SCORE_ALREADY_EXIST(false, HttpStatus.BAD_REQUEST, 2554, "이미 평가 점수가 등록되어 있습니다."),


    //  안내 메일 - 2600 ~ 2699
    EMPLOYMENT_MAIL_TEMPLATE_DUPLICATE_TITLE(false, HttpStatus.CONFLICT, 2600, "이름이 중복된 템플릿이 존재합니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NO_TITLE(false, HttpStatus.BAD_REQUEST, 2601, "제목을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NO_CONTENT(false, HttpStatus.BAD_REQUEST, 2602, "내용을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_TEMPLATE_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2603, "존재하지 않는 템플릿입니다."),

    EMPLOYMENT_MAIL_NOT_FOUND(false, HttpStatus.BAD_REQUEST, 2630, "요청한 메일을 찾을 수 없습니다."),
    EMPLOYMENT_MAIL_NO_CONTENT(false, HttpStatus.BAD_REQUEST, 2631, "메일의 내용을 입력하지 않았습니다."),
    EMPLOYMENT_MAIL_INADEQUATE_EMAIL(false, HttpStatus.CONFLICT, 2632, "유효하지 않은 형태의 이메일입니다."),
    EMPLOYMENT_MAIL_NO_TITLE(false, HttpStatus.BAD_REQUEST, 2633, "메일의 제목을 입력하지 않았습니다."),

    // 인증 2700 ~ 2799
    INVALID_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, 2700, "유효하지 않은 Refresh Token입니다."),
    MEMBER_EMPLOYEE_NUMBER_DUPLICATE(false, HttpStatus.UNAUTHORIZED, 2701, "중복된 사번입니다.");

    private final boolean success;
    private final HttpStatus httpStatus;        // HTTP 상태 코드
    private final int code;                     // 비지니스 로직
    private final String message;
}
