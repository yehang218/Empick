-- =====================================================
-- Member 및 Member_Edit 테이블 인덱스
-- =====================================================
-- 기존 common_index.sql에 추가할 내용

-- =====================================================
-- Member 테이블 인덱스
-- =====================================================

-- 1. 기본 조회 인덱스 (이미 UNIQUE 제약조건이 있는 것들은 제외)
CREATE INDEX IF NOT EXISTS `idx_member_name` ON `member`(`name`);
CREATE INDEX IF NOT EXISTS `idx_member_phone` ON `member`(`phone`);
CREATE INDEX IF NOT EXISTS `idx_member_status` ON `member`(`status`);

-- 2. 외래키 참조 인덱스
CREATE INDEX IF NOT EXISTS `idx_member_department_id` ON `member`(`department_id`);
CREATE INDEX IF NOT EXISTS `idx_member_position_id` ON `member`(`position_id`);
CREATE INDEX IF NOT EXISTS `idx_member_job_id` ON `member`(`job_id`);
CREATE INDEX IF NOT EXISTS `idx_member_rank_id` ON `member`(`rank_id`);
CREATE INDEX IF NOT EXISTS `idx_member_created_member_id` ON `member`(`created_member_id`);
CREATE INDEX IF NOT EXISTS `idx_member_deleted_member_id` ON `member`(`deleted_member_id`);
CREATE INDEX IF NOT EXISTS `idx_member_updated_member_id` ON `member`(`updated_member_id`);

-- 3. 날짜 관련 인덱스 (HR 시스템에서 중요)
CREATE INDEX IF NOT EXISTS `idx_member_hire_at` ON `member`(`hire_at`);
CREATE INDEX IF NOT EXISTS `idx_member_resign_at` ON `member`(`resign_at`);
CREATE INDEX IF NOT EXISTS `idx_member_last_login_at` ON `member`(`last_login_at`);
CREATE INDEX IF NOT EXISTS `idx_member_created_at` ON `member`(`created_at`);
CREATE INDEX IF NOT EXISTS `idx_member_updated_at` ON `member`(`updated_at`);

-- 4. 생년월일 인덱스 (연령대별 조회 등에 활용)
CREATE INDEX IF NOT EXISTS `idx_member_birth` ON `member`(`birth`);

-- 5. 휴가 일수 인덱스 (휴가 관리용)
CREATE INDEX IF NOT EXISTS `idx_member_vacation_count` ON `member`(`vacation_count`);

-- =====================================================
-- Member 테이블 복합 인덱스 (성능 최적화)
-- =====================================================

-- 6. 활성 사원 조회 최적화
CREATE INDEX IF NOT EXISTS `idx_member_status_department` ON `member`(`status`, `department_id`);
CREATE INDEX IF NOT EXISTS `idx_member_status_position` ON `member`(`status`, `position_id`);
CREATE INDEX IF NOT EXISTS `idx_member_status_hire_at` ON `member`(`status`, `hire_at`);

-- 7. 부서별 직책 조회 최적화
CREATE INDEX IF NOT EXISTS `idx_member_dept_position` ON `member`(`department_id`, `position_id`);
CREATE INDEX IF NOT EXISTS `idx_member_dept_rank` ON `member`(`department_id`, `rank_id`);

-- 8. 재직 상태 확인 최적화 (퇴사일이 없는 = 재직중)
CREATE INDEX IF NOT EXISTS `idx_member_resign_status` ON `member`(`resign_at`, `status`);

-- 9. 로그인 관련 최적화
CREATE INDEX IF NOT EXISTS `idx_member_email_status` ON `member`(`email`, `status`);
CREATE INDEX IF NOT EXISTS `idx_member_employee_status` ON `member`(`employee_number`, `status`);

-- 10. 입사년도별 통계용
CREATE INDEX IF NOT EXISTS `idx_member_hire_year` ON `member`((YEAR(`hire_at`)));
CREATE INDEX IF NOT EXISTS `idx_member_hire_month` ON `member`((YEAR(`hire_at`)), (MONTH(`hire_at`)));

-- 11. 이름 검색 최적화 (부분 검색용)
CREATE INDEX IF NOT EXISTS `idx_member_name_prefix` ON `member`(`name`(10));

-- =====================================================
-- Member_Edit 테이블 인덱스
-- =====================================================

-- 12. 기본 조회 인덱스
CREATE INDEX IF NOT EXISTS `idx_member_edit_member_id` ON `member_edit`(`member_id`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_reviewer_id` ON `member_edit`(`reviewer_id`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_status` ON `member_edit`(`status`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_target_field` ON `member_edit`(`target_field`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_field_type` ON `member_edit`(`field_type`);

-- 13. 날짜 관련 인덱스
CREATE INDEX IF NOT EXISTS `idx_member_edit_requested_at` ON `member_edit`(`requested_at`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_updated_at` ON `member_edit`(`updated_at`);

-- =====================================================
-- Member_Edit 테이블 복합 인덱스
-- =====================================================

-- 14. 승인 대기 요청 조회 최적화
CREATE INDEX IF NOT EXISTS `idx_member_edit_status_requested` ON `member_edit`(`status`, `requested_at`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_reviewer_status` ON `member_edit`(`reviewer_id`, `status`);

-- 15. 특정 사원의 요청 내역 조회 최적화
CREATE INDEX IF NOT EXISTS `idx_member_edit_member_status` ON `member_edit`(`member_id`, `status`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_member_requested` ON `member_edit`(`member_id`, `requested_at`);

-- 16. 필드별 요청 통계용
CREATE INDEX IF NOT EXISTS `idx_member_edit_field_status` ON `member_edit`(`target_field`, `status`);
CREATE INDEX IF NOT EXISTS `idx_member_edit_field_requested` ON `member_edit`(`target_field`, `requested_at`);

-- 17. 일별/월별 요청 통계용
CREATE INDEX IF NOT EXISTS `idx_member_edit_requested_date` ON `member_edit`((DATE(`requested_at`)));
CREATE INDEX IF NOT EXISTS `idx_member_edit_requested_month` ON `member_edit`((YEAR(`requested_at`)), (MONTH(`requested_at`)));

-- =====================================================
-- 고급 성능 최적화 인덱스
-- =====================================================

-- 18. 조직도 조회 최적화 (부서 → 직책 → 직급 순서)
CREATE INDEX IF NOT EXISTS `idx_member_org_chart` ON `member`(`department_id`, `position_id`, `rank_id`, `status`);

-- 19. 전체 사원 목록 조회 최적화 (페이징용)
CREATE INDEX IF NOT EXISTS `idx_member_list_paging` ON `member`(`status`, `created_at`, `id`);

-- 20. 검색 기능 최적화 (이름, 사번, 이메일 통합)
CREATE INDEX IF NOT EXISTS `idx_member_search_name_email` ON `member`(`name`, `email`);

-- 21. 승인자별 요청 처리 통계
CREATE INDEX IF NOT EXISTS `idx_member_edit_reviewer_summary` ON `member_edit`(`reviewer_id`, `status`, `requested_at`);

-- =====================================================
-- 인덱스 생성 완료 확인
-- =====================================================
SELECT 'Member and Member_Edit indexes created successfully!' as message;