INSERT INTO role (id, code, name, description, created_at, updated_at, deleted_at)
VALUES (
           2000,
           'ROLE_SAMPLE',
           '샘플권한',
           '이것은 샘플 권한입니다.',
           NOW(),
           NOW(),
           NULL
       );

INSERT INTO `rank` (id, name, code, is_active, created_at, updated_at, salary_band, role_id)
VALUES (
           2000,
           '샘플직급',
           'RANK_SAMPLE',
           1,
           NOW(),
           NOW(),
           5,
           2000
       );

INSERT INTO `position` (
    id,
    name,
    is_active,
    created_at,
    updated_at,
    description,
    role_id
) VALUES (
             2000,
             '샘플직책',
             1,
             NOW(),
             NOW(),
             '이것은 샘플 직책입니다.',
             2000
         );

INSERT INTO `job` (
    id,
    name,
    code,
    is_active,
    created_at,
    updated_at,
    description,
    role_id
) VALUES (
             2000,
             '샘플직무',
             'JOB_SAMPLE',
             1,
             NOW(),
             NOW(),
             '이것은 샘플 직무입니다.',
             2000
         );

INSERT INTO `department` (
    id,
    name,
    code,
    created_at,
    updated_at,
    is_active,
    description,
    role_id
) VALUES (
             2000,
             '샘플부서',
             'DEPT_SAMPLE',
             NOW(),
             NOW(),
             1,
             '이것은 샘플 부서입니다.',
             2000
         );

INSERT INTO member (
    id,
    password,
    employee_number,
    name,
    birth,
    phone,
    picture_url,
    email,
    address,
    vacation_count,
    hire_at,
    resign_at,
    created_at,
    updated_at,
    created_member_id,
    deleted_member_id,
    updated_member_id,
    last_login_at,
    status,
    department_id,
    position_id,
    job_id,
    rank_id
) VALUES (
             2000,
             'hashed_password_sample', -- 실제 사용 시 비밀번호는 해시값으로 저장
             2000,
             '홍길동',
             '1990-01-01',
             '010-1234-5678',
             'https://s3.example.com/profile/sample.jpg',
             'sample2000@example.com',
             '서울시 강남구 테헤란로 123',
             15,
             NOW(),
             NULL,
             NOW(),
             NOW(),
             NULL,
             NULL,
             NULL,
             NULL,
             1,
             2000, -- department_id
             2000, -- position_id
             2000, -- job_id
             2000 -- rank_id
         );

INSERT INTO introduce_template_item (id, title, member_id) VALUES
                                                               (2000, '자기주도성 평가', 2000),
                                                               (2001, '팀워크 능력 평가', 2000),
                                                               (2002, '문제해결 능력 평가', 2000),
                                                               (2003, '기술 역량 평가', 2000),
                                                               (2004, '커뮤니케이션 능력 평가', 2000);

INSERT INTO introduce_template (id, title, member_id, introduce_template_item_id) VALUES
                                                                                      (2000, '자기주도성 평가 템플릿', 2000, 2000),
                                                                                      (2001, '팀워크 능력 평가 템플릿', 2000, 2001),
                                                                                      (2002, '문제해결 능력 평가 템플릿', 2000, 2002),
                                                                                      (2003, '기술 역량 평가 템플릿', 2000, 2003),
                                                                                      (2004, '커뮤니케이션 능력 평가 템플릿', 2000, 2004);

INSERT INTO introduce (
    id,
    content,
    member_id,
    introduce_template_id,
    updated_by,
    updated_at
) VALUES (
             2000,
             '저는 문제 해결에 강한 책임감을 가지고 임하는 사람입니다.',
             2000,
             2000,
             NULL,
             NULL
         );

INSERT INTO introduce_standard_item (id, content, member_id, updated_by, updated_at) VALUES
                                                                                         (2000, '논리적 구성 평가 기준', 2000, NULL, NULL),
                                                                                         (2001, '핵심 내용 포함 여부 평가', 2000, NULL, NULL),
                                                                                         (2002, '문장 표현력 평가', 2000, NULL, NULL),
                                                                                         (2003, '직무 적합성 평가', 2000, NULL, NULL),
                                                                                         (2004, '성실성/태도 평가', 2000, NULL, NULL);

INSERT INTO introduce_standard (
    id,
    content,
    member_id,
    introduce_id,
    introduce_standard_item_id,
    updated_by,
    updated_at
) VALUES
      (2000, '논리적인 흐름이 잘 구성되어 있음', 2000, 2000, 2000, NULL, NULL),
      (2001, '핵심 경험이 잘 강조되어 있음', 2000, 2000, 2001, NULL, NULL),
      (2002, '문장이 간결하고 명확함', 2000, 2000, 2002, NULL, NULL),
      (2003, '직무와 관련된 역량이 잘 드러남', 2000, 2000, 2003, NULL, NULL),
      (2004, '지원자의 성실성이 잘 표현됨', 2000, 2000, 2004, NULL, NULL);

INSERT INTO introduce_rating_result (
    id,
    member_id,
    content,
    rating_score,
    introduce_standard_id,
    status,
    updated_by,
    updated_at
) VALUES
      (2000, 2000, '논리적 구성에 대한 평가: 매우 논리적이고 일관됨.', 5, 2000, 1, NULL, NULL),
      (2001, 2000, '핵심 내용이 명확하게 드러나 있음.', 4, 2001, 1, NULL, NULL),
      (2002, 2000, '문장이 간결하고 표현력이 뛰어남.', 5, 2002, 1, NULL, NULL),
      (2003, 2000, '직무와의 연관성이 약간 부족함.', 3, 2003, 2, NULL, NULL),
      (2004, 2000, '성실성이 잘 표현되어 있음.', 4, 2004, 1, NULL, NULL);

INSERT INTO applicant (
    id,
    name,
    phone,
    email,
    profile_url,
    birth,
    address
) VALUES (
             2000,
             '서민종',
             '010-2643-7581',
             'tjalswhd1@naver.com',
             'https://s3.example.com/profile/applicant2000.jpg',
             '1997-02-26',
             '서울특별시 중랑구 면목동'
         );

INSERT INTO recruitment_request (
    id,
    headcount,
    started_at,
    ended_at,
    qualification,
    preference,
    responsibility,
    employment_type,
    work_location,
    created_at,
    member_id,
    department_id
) VALUES (
             2000,
             3,
             '2025-07-01 09:00:00',
             '2025-07-31 18:00:00',
             '관련 전공자, 3년 이상 경력 우대',
             '팀워크가 뛰어난 인재',
             '웹 서비스 백엔드 개발 및 유지보수',
             '정규직',
             '서울 본사',
             NOW(),
             2000,
             2000
         );

INSERT INTO recruitment_template (
    id,
    name,
    created_at,
    is_deleted,
    member_id
) VALUES (
             2000,
             '백엔드 개발자 채용 템플릿',
             NOW(),
             'N',
             2000
         );

INSERT INTO recruitment (
    id,
    title,
    content,
    recruit_type,
    status,
    image_url,
    started_at,
    ended_at,
    created_at,
    updated_at,
    deleted_at,
    member_id,
    recruitment_template_id,
    introduce_template_id,
    recruitment_request_id
) VALUES (
             2000,
             '백엔드 개발자 채용 공고',
             '경력 3년 이상 백엔드 개발자 모집합니다. 주요 업무는 API 개발 및 운영입니다.',
             1,
             0,
             'https://example.com/images/recruitment_2000.png',
             '2025-07-01 09:00:00',
             '2025-07-31 18:00:00',
             NOW(),
             NOW(),
             NULL,
             2000,
             2000,
             2000,
             2000
         );

INSERT INTO application (
    id,
    recruitment_id,
    created_at,
    status,
    applicant_id,
    introduce_rating_result_id,
    updated_at,
    updated_by
) VALUES (
             2000,
             2000,
             NOW(),
             1,
             2000,
             2000,
             NOW(),
             2000
         );

INSERT INTO job_test (
    id,
    title,
    difficulty,
    test_time,
    started_at,
    ended_at,
    created_at,
    updated_at,
    created_member_id,
    updated_member_id
) VALUES (
             2000,
             '백엔드 개발자 코딩 테스트',
             3,
             90,
             '2025-07-10 10:00:00',
             '2025-07-10 11:30:00',
             NOW(),
             NULL,
             2000,
             NULL
         );

INSERT INTO job_test_evaluation_criteria (
    id,
    content,
    detail_content,
    score_weight,
    job_test_id
) VALUES (
             2000,
             '코드 품질',
             '가독성, 일관된 스타일, 불필요한 중복 없이 클린코드 원칙을 잘 지켰는가?',
             0.4,
             2000
         );

INSERT INTO application_job_test (
    id,
    evaluator_comment,
    submitted_at,
    grading_total_score,
    evaluation_score,
    grading_status,
    evaluation_status,
    entry_code,
    application_id,
    job_test_id,
    member_id
) VALUES (
             2000,
             NULL,
             NOW(),
             0.0,
             0.0,
             0,
             0,
             'CODE-2000-XYZ',
             2000,
             2000,
             2000
         );

INSERT INTO job_test_evaluation_result (
    id,
    evaluator_comment,
    score,
    application_job_test_id,
    job_test_evaluation_criteria_id
) VALUES (
             2000,
             '가독성이 좋고 로직이 명확하게 작성되어 있음',
             85,
             2000,
             2000
         );

INSERT INTO question (
    id,
    content,
    detail_content,
    type,
    difficulty,
    answer,
    created_at,
    updated_at,
    created_member_id,
    updated_member_id
) VALUES
      (2000, '1+1은?', '기본적인 덧셈 문제입니다.', 1, 1, '2', NOW(), NULL, 2000, NULL),
      (2001, 'HTML의 의미는?', '웹 프론트엔드의 기초 기술입니다.', 1, 2, 'HyperText Markup Language', NOW(), NULL, 2000, NULL),
      (2002, 'OOP의 4가지 특징 중 하나를 말하시오.', NULL, 2, 2, NULL, NOW(), NULL, 2000, NULL),
      (2003, '리눅스에서 디렉토리 변경 명령어는?', NULL, 1, 1, 'cd', NOW(), NULL, 2000, NULL),
      (2004, 'SQL에서 데이터를 조회하는 기본 명령은?', NULL, 1, 1, 'SELECT', NOW(), NULL, 2000, NULL),
      (2005, 'TCP와 UDP의 차이를 설명하시오.', NULL, 2, 3, NULL, NOW(), NULL, 2000, NULL),
      (2006, 'Python에서 리스트를 정렬하는 함수는?', NULL, 1, 1, 'sorted', NOW(), NULL, 2000, NULL),
      (2007, '자바에서 클래스와 객체의 차이점은?', NULL, 2, 2, NULL, NOW(), NULL, 2000, NULL),
      (2008, '웹에서 CORS는 무엇인가?', NULL, 2, 3, NULL, NOW(), NULL, 2000, NULL),
      (2009, '브라우저 렌더링 과정에 대해 설명하시오.', NULL, 2, 3, NULL, NOW(), NULL, 2000, NULL);

INSERT INTO job_test_question (
    id,
    score,
    option_number,
    job_test_id,
    question_id
) VALUES
      (2000, 10, 4, 2000, 2000),
      (2001, 10, 4, 2000, 2001),
      (2002, 15, 0, 2000, 2002),
      (2003, 10, 4, 2000, 2003),
      (2004, 10, 4, 2000, 2004),
      (2005, 20, 0, 2000, 2005),
      (2006, 10, 4, 2000, 2006),
      (2007, 15, 0, 2000, 2007),
      (2008, 20, 0, 2000, 2008),
      (2009, 20, 0, 2000, 2009);

INSERT INTO interview_sheet (
    id,
    name,
    is_deleted,
    member_id,
    updated_at
) VALUES (
             2000,
             '기본 면접 평가표',
             'N',
             2000,
             NOW()
         );

INSERT INTO interview_criteria (
    id,
    content,
    detail_content,
    is_deleted,
    member_id,
    updated_at
) VALUES
      (2000, '전문성', '지원 직무와 관련된 전문적인 지식과 기술 수준을 평가합니다.', 'N', 2000, NOW()),
      (2001, '의사소통 능력', '질문에 대한 명확한 답변과 커뮤니케이션 능력을 확인합니다.', 'N', 2000, NOW()),
      (2002, '문제 해결 능력', '상황에 따른 분석력 및 문제 해결 방식에 대해 평가합니다.', 'N', 2000, NOW()),
      (2003, '조직 적응력', '조직 문화에 적응하고 팀워크를 발휘할 수 있는지 평가합니다.', 'N', 2000, NOW()),
      (2004, '성장 가능성', '자기 개발 의지와 장기적인 성장 가능성을 중심으로 평가합니다.', 'N', 2000, NOW());

INSERT INTO interview_sheet_item (id, sheet_id, criteria_id, weight, member_id)
VALUES
    (2000, 2000, 2000, 0.2, 2000),
    (2001, 2000, 2001, 0.25, 2000),
    (2002, 2000, 2002, 0.15, 2000),
    (2003, 2000, 2003, 0.2, 2000),
    (2004, 2000, 2004, 0.2, 2000);

INSERT INTO interview (id, application_id, sheet_id, datetime, address, score, interview_review)
VALUES (2000, 2000, 2000, '2025-06-11 10:00:00', 'https://zoom.us/j/1234567890?pwd=abcdefg', NULL, NULL);

INSERT INTO interviewer (id, interview_id, interviewer_id, score, review)
VALUES (2000, 2000, 2000, NULL, NULL);

INSERT INTO interview_score (id, interviewer_id, item_id, score, review) VALUES
                                                                             (2000, 2000, 2000, 85, '평가 코멘트 1'),
                                                                             (2001, 2000, 2001, 90, '평가 코멘트 2'),
                                                                             (2002, 2000, 2002, 88, '평가 코멘트 3'),
                                                                             (2003, 2000, 2003, 92, '평가 코멘트 4'),
                                                                             (2004, 2000, 2004, 87, '평가 코멘트 5');

INSERT INTO answer (id, content, attempt, is_correct, score, application_job_test_id, question_id) VALUES
                                                                                                       (2000, '답변 내용 1', 1, 1, 20,2000, 2000),
                                                                                                       (2001, '답변 내용 2', 1, 0, 20,2000, 2001),
                                                                                                       (2002, '답변 내용 3', 1, 1, 20,2000, 2002),
                                                                                                       (2003, '답변 내용 4', 1, 1, 20,2000, 2003),
                                                                                                       (2004, '답변 내용 5', 1, 0, 20,2000, 2004),
                                                                                                       (2005, '답변 내용 6', 1, 1, 20,2000, 2005),
                                                                                                       (2006, '답변 내용 7', 1, 0, 20,2000, 2006),
                                                                                                       (2007, '답변 내용 8', 1, 1, 20,2000, 2007),
                                                                                                       (2008, '답변 내용 9', 1, 1, 20,2000, 2008),
                                                                                                       (2009, '답변 내용 10', 1, 0, 20,2000, 2009);

INSERT INTO application_item_category (id, name, input_type, display_order, application_item_category_id) VALUES
                                                                                                              (2000, '개인 정보', 1, 1, NULL),
                                                                                                              (2001, '학력 사항', 2, 2, NULL),
                                                                                                              (2002, '경력 사항', 2, 3, NULL),
                                                                                                              (2003, '자기소개', 1, 4, NULL),
                                                                                                              (2004, '기타 정보', 3, 5, NULL);

INSERT INTO application_item (is_required, application_item_category_id, recruitment_id) VALUES
                                                                                             ('Y', 2000, 2000),
                                                                                             ('N', 2001, 2000),
                                                                                             ('Y', 2002, 2000),
                                                                                             ('N', 2003, 2000),
                                                                                             ('Y', 2004, 2000);