-- =====================================================
-- Common Index Creation Script for MariaDB
-- =====================================================
-- 이 스크립트는 데이터베이스 초기화 시 공통으로 사용되는 인덱스들을 생성합니다.
-- 테이블 생성 후에 실행하세요.

-- =====================================================
-- Role 테이블 인덱스
-- =====================================================
CREATE INDEX IF NOT EXISTS `idx_role_code` ON `role`(`code`);
CREATE INDEX IF NOT EXISTS `idx_role_active` ON `role`(`is_active`);
CREATE INDEX IF NOT EXISTS `idx_role_code_active` ON `role`(`code`, `is_active`);

-- =====================================================
-- Position 테이블 인덱스
-- =====================================================
CREATE INDEX IF NOT EXISTS `idx_position_code` ON `position`(`code`);
CREATE INDEX IF NOT EXISTS `idx_position_active` ON `position`(`is_active`);
CREATE INDEX IF NOT EXISTS `idx_position_code_active` ON `position`(`code`, `is_active`);
CREATE INDEX IF NOT EXISTS `idx_position_role_id` ON `position`(`role_id`);
CREATE INDEX IF NOT EXISTS `idx_position_name` ON `position`(`name`);

-- =====================================================
-- Department 테이블 인덱스
-- =====================================================
CREATE INDEX IF NOT EXISTS `idx_department_code` ON `department`(`code`);
CREATE INDEX IF NOT EXISTS `idx_department_active` ON `department`(`is_active`);
CREATE INDEX IF NOT EXISTS `idx_department_code_active` ON `department`(`code`, `is_active`);
CREATE INDEX IF NOT EXISTS `idx_department_role_id` ON `department`(`role_id`);
CREATE INDEX IF NOT EXISTS `idx_department_name` ON `department`(`name`);

-- =====================================================
-- Job 테이블 인덱스
-- =====================================================
CREATE INDEX IF NOT EXISTS `idx_job_code` ON `job`(`code`);
CREATE INDEX IF NOT EXISTS `idx_job_active` ON `job`(`is_active`);
CREATE INDEX IF NOT EXISTS `idx_job_code_active` ON `job`(`code`, `is_active`);
CREATE INDEX IF NOT EXISTS `idx_job_role_id` ON `job`(`role_id`);
CREATE INDEX IF NOT EXISTS `idx_job_name` ON `job`(`name`);

-- =====================================================
-- Rank 테이블 인덱스
-- =====================================================
CREATE INDEX IF NOT EXISTS `idx_rank_code` ON `rank`(`code`);
CREATE INDEX IF NOT EXISTS `idx_rank_active` ON `rank`(`is_active`);
CREATE INDEX IF NOT EXISTS `idx_rank_code_active` ON `rank`(`code`, `is_active`);
CREATE INDEX IF NOT EXISTS `idx_rank_role_id` ON `rank`(`role_id`);
CREATE INDEX IF NOT EXISTS `idx_rank_name` ON `rank`(`name`);
CREATE INDEX IF NOT EXISTS `idx_rank_salary_band` ON `rank`(`salary_band`);

-- =====================================================
-- 공통 날짜 관련 인덱스 (모든 테이블에 적용)
-- =====================================================
-- 생성일 기준 조회를 위한 인덱스
CREATE INDEX IF NOT EXISTS `idx_role_created_at` ON `role`(`created_at`);
CREATE INDEX IF NOT EXISTS `idx_position_created_at` ON `position`(`created_at`);
CREATE INDEX IF NOT EXISTS `idx_department_created_at` ON `department`(`created_at`);
CREATE INDEX IF NOT EXISTS `idx_job_created_at` ON `job`(`created_at`);
CREATE INDEX IF NOT EXISTS `idx_rank_created_at` ON `rank`(`created_at`);

-- 수정일 기준 조회를 위한 인덱스
CREATE INDEX IF NOT EXISTS `idx_role_updated_at` ON `role`(`updated_at`);
CREATE INDEX IF NOT EXISTS `idx_position_updated_at` ON `position`(`updated_at`);
CREATE INDEX IF NOT EXISTS `idx_department_updated_at` ON `department`(`updated_at`);
CREATE INDEX IF NOT EXISTS `idx_job_updated_at` ON `job`(`updated_at`);
CREATE INDEX IF NOT EXISTS `idx_rank_updated_at` ON `rank`(`updated_at`);

-- =====================================================
-- 복합 인덱스 (성능 최적화)
-- =====================================================
-- 활성 상태와 생성일 복합 인덱스
CREATE INDEX IF NOT EXISTS `idx_role_active_created` ON `role`(`is_active`, `created_at`);
CREATE INDEX IF NOT EXISTS `idx_position_active_created` ON `position`(`is_active`, `created_at`);
CREATE INDEX IF NOT EXISTS `idx_department_active_created` ON `department`(`is_active`, `created_at`);
CREATE INDEX IF NOT EXISTS `idx_job_active_created` ON `job`(`is_active`, `created_at`);
CREATE INDEX IF NOT EXISTS `idx_rank_active_created` ON `rank`(`is_active`, `created_at`);

-- =====================================================
-- 전문 검색을 위한 인덱스 (필요시 사용)
-- =====================================================
-- 이름 검색 최적화를 위한 인덱스
CREATE INDEX IF NOT EXISTS `idx_role_name_prefix` ON `role`(`name`(10));
CREATE INDEX IF NOT EXISTS `idx_position_name_prefix` ON `position`(`name`(10));
CREATE INDEX IF NOT EXISTS `idx_department_name_prefix` ON `department`(`name`(10));
CREATE INDEX IF NOT EXISTS `idx_job_name_prefix` ON `job`(`name`(10));
CREATE INDEX IF NOT EXISTS `idx_rank_name_prefix` ON `rank`(`name`(10));

-- =====================================================
-- 통계 및 분석을 위한 인덱스
-- =====================================================
-- 월별 생성 통계를 위한 인덱스
CREATE INDEX IF NOT EXISTS `idx_role_created_month` ON `role`((YEAR(created_at)), (MONTH(created_at)));
CREATE INDEX IF NOT EXISTS `idx_position_created_month` ON `position`((YEAR(created_at)), (MONTH(created_at)));
CREATE INDEX IF NOT EXISTS `idx_department_created_month` ON `department`((YEAR(created_at)), (MONTH(created_at)));
CREATE INDEX IF NOT EXISTS `idx_job_created_month` ON `job`((YEAR(created_at)), (MONTH(created_at)));
CREATE INDEX IF NOT EXISTS `idx_rank_created_month` ON `rank`((YEAR(created_at)), (MONTH(created_at)));

-- =====================================================
-- 인덱스 생성 완료 메시지
-- =====================================================
SELECT 'Common indexes created successfully!' as message;