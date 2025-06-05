INSERT INTO role (id, code, name, description, created_at, updated_at, deleted_at)
VALUES (1, 'ROLE_HR_ACCESS', '인사팀기능접근', '인사팀 전용 기능에 접근 가능
', '2025-06-03 17:16:55', '2025-06-03 17:17:10', NULL);
INSERT INTO role (id, code, name, description, created_at, updated_at, deleted_at)
VALUES (2, 'ROLE_RECRUITMENT_PLAN_EDITOR', '채용계획서작성
', '채용계획서 작성 기능에 접근 가능
', '2025-06-03 17:25:44', '2025-06-03 17:25:45', NULL);
INSERT INTO role (id, code, name, description, created_at, updated_at, deleted_at)
VALUES (3, 'ROLE_APPROVAL_PROCESSOR', '결재처리권한
', '결재처리권한을 갖을수 있음
', '2025-06-03 17:26:09', '2025-06-03 17:26:10', NULL);
INSERT INTO role (id, code, name, description, created_at, updated_at, deleted_at)
VALUES (4, 'ROLE_RECRUITMENT_OPERATOR', '채용진행자', '채용 관련한 모든 기능에 접근 가능
', '2025-06-03 17:27:52', '2025-06-03 17:27:53', NULL);

INSERT INTO `department` (name, code, description, role_id)
VALUES
    ('인사부', 'HR001', '사내 인사 업무 담당', 1),
    ('기획팀', 'PLN001', '전사 전략 기획 부서', 2),
    ('개발팀', 'DEV001', '웹/모바일 서비스 개발 담당', 3);

INSERT INTO `job` (name, code, description, role_id)
VALUES
    ('백엔드 개발자', 'JOB_BE01', '서버 및 API 개발', 3),
    ('프론트엔드 개발자', 'JOB_FE01', 'UI 및 사용자 경험 개발', 3),
    ('기획자', 'JOB_PL01', '서비스 및 정책 기획', 2);

INSERT INTO `position` (name, description)
VALUES
    ('팀장', '팀 단위 조직 리더 역할'),
    ('파트장', '파트 단위 실무 총괄'),
    ('사원', '일반 실무 담당자');

INSERT INTO `rank` (name, code, salary_band, role_id)
VALUES
    ('주임', 'RANK_JR', 1, 1),
    ('대리', 'RANK_AM', 2, 1),
    ('과장', 'RANK_MGR', 3, 2);