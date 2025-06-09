DROP TABLE IF EXISTS `department_head`;
DROP TABLE IF EXISTS `member_edit`;
DROP TABLE IF EXISTS `member`;

CREATE TABLE `member`
(
    `id`                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '사원 테이블 ID',
    `password`          VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `employee_number`   INT          NOT NULL COMMENT '사번',
    `name`              VARCHAR(255) NOT NULL COMMENT '사원 이름',
    `birth`             VARCHAR(255) NULL COMMENT 'YYYY-MM-DD',
    `phone`             VARCHAR(255) NOT NULL COMMENT '010-1234-5678',
    `picture_url`       VARCHAR(255) NOT NULL COMMENT 'S3 path',
    `email`             VARCHAR(255) NOT NULL COMMENT '이메일',
    `address`           VARCHAR(255) NOT NULL COMMENT '주소',
    `vacation_count`    INT          NOT NULL DEFAULT 0 COMMENT '휴가 일수',
    `hire_at`           DATETIME     NOT NULL COMMENT '입사일',
    `resign_at`         DATETIME     NULL COMMENT '퇴사일',
    `created_at`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `updated_at`        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    `created_member_id` INT          NULL COMMENT '입사처리자',
    `deleted_member_id` INT          NULL COMMENT '퇴사처리자',
    `updated_member_id` INT          NULL COMMENT '수정자',
    `last_login_at`     DATETIME     NULL COMMENT '로그인 시작',
    `status`            TINYINT      NOT NULL COMMENT '계정 상태 (0 = 비활성, 1 = 활성)',
    `department_id`     INT          NULL COMMENT '부서',
    `position_id`       INT          NULL COMMENT '직책',
    `job_id`            INT          NULL COMMENT '직무',
    `rank_id`           INT          NULL COMMENT '직급',
    CONSTRAINT `uk_member_employee_number` UNIQUE (`employee_number`),
    CONSTRAINT `uk_member_email` UNIQUE (`email`),
    CONSTRAINT `fk_member_department`
        FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_member_position_id`
        FOREIGN KEY (`position_id`) REFERENCES `position` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_member_job_id`
        FOREIGN KEY (`job_id`) REFERENCES `job` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_member_rank_id`
        FOREIGN KEY (`rank_id`) REFERENCES `rank` (`id`) ON DELETE CASCADE
) COMMENT = '사원 테이블';

CREATE TABLE `member_edit`
(
    `id`                 INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '사원 정보 수정 요청 ID',
    `member_id`          INT          NOT NULL COMMENT '대상 사원 ID',
    `reviewer_id` INT          NULL COMMENT '승인자 ID',
    `target_field`       VARCHAR(255) NOT NULL COMMENT '수정 요청 대상 필드',
    `original_value`     VARCHAR(255) NOT NULL COMMENT '기존 값',
    `requested_value`    VARCHAR(255) NOT NULL COMMENT '수정 요청 값',
    `field_type`         TINYINT      NOT NULL COMMENT '필드 타입 (0=String, 1=Int, 2=Datetime)',
    `status`             TINYINT      NOT NULL DEFAULT 0 COMMENT '요청 상태 (0=PENDING, 1=APPROVED, 2=REJECTED)',
    `requested_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '요청 생성일시',
    `reason`             TEXT         NOT NULL COMMENT '수정 요청 사유',
    `updated_at`         DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    CONSTRAINT `fk_member_edit_member`
        FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_member_edit_approver`
        FOREIGN KEY (`reviewer_id`) REFERENCES `member` (`id`) ON DELETE SET NULL
) COMMENT = '사원 정보 수정 요청 테이블';

#status는
#트리거 적용 가능
CREATE TABLE `member_edit`
(
    `id`                 INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '사원 테이블 ID',
    `member_id`          INT          NOT NULL COMMENT '요청 사원 ID',
    `reviewer_id` INT          NULL COMMENT '승인자 ID',
    `target_field`       VARCHAR(255) NOT NULL COMMENT '변경을 원하는 속성',
    `original_value`     VARCHAR(255) NOT NULL COMMENT '이전 값',
    `requested_value`    VARCHAR(255) NOT NULL COMMENT '변경을 원하는 값',
    `field_type`         TINYINT      NOT NULL COMMENT '속성 타입 (0 = String, 1 = int, 2 = datetime)',
    `status`             TINYINT      NOT NULL DEFAULT 0 COMMENT '처리 상태 (0 = PENDING, 1 = APPROVED, 2 =REJECTED)',
    `requested_at`       DATETIME     NOT NULL COMMENT '요청시각',
    `reason`             TEXT         NOT NULL COMMENT '변경 사유',
    `updated_at`         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    CONSTRAINT `fk_member_edit_member`
        FOREIGN KEY (`member_id`) REFERENCES `member` (`id`),
    CONSTRAINT `fk_member_edit_approver`
        FOREIGN KEY (`reviewer_id`) REFERENCES `member` (`id`) ON DELETE SET NULL
) COMMENT = '사원 정보 수정 요청 테이블';


CREATE TABLE `department_head`
(
    `department_id` INT NOT NULL COMMENT '부서 id',
    `member_id`     INT NOT NULL COMMENT '사원 id',
    PRIMARY KEY (`department_id`, `member_id`),
    CONSTRAINT `fk_department_head_department`
        FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_department_head_member`
        FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) COMMENT = '부서장 테이블 = 사원(N) : 부서(M)';