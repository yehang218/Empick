DROP TABLE IF EXISTS `department`;
DROP TABLE IF EXISTS `position`;
DROP TABLE IF EXISTS `job`;
DROP TABLE IF EXISTS `rank`;

CREATE TABLE `department`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '부서 id',
    `name`        VARCHAR(255) NOT NULL COMMENT '부서 이름',
    `code`        VARCHAR(255) NOT NULL COMMENT '부서 코드',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    `is_active`   BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '활성여부',
    `description` TEXT         NULL COMMENT '설명',
    `role_id`     INT          NULL COMMENT '권한 id',
    CONSTRAINT `fk_department_role`
        FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
)
    COMMENT = '부서 테이블';

CREATE TABLE `position`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '직책 id',
    `name`        VARCHAR(255) NOT NULL COMMENT '직책 이름',
    `is_active`   BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '활성여부',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    `description` TEXT         NULL COMMENT '설명',
    `role_id`     INT          NULL COMMENT '권한 id',
    CONSTRAINT `fk_position_role`
        FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
)
    COMMENT = '직책 테이블';

CREATE TABLE `job`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '직무 id',
    `name`        VARCHAR(255) NOT NULL COMMENT '직무 이름',
    `code`        VARCHAR(255) NOT NULL COMMENT '직무 코드',
    `is_active`   TINYINT      NOT NULL DEFAULT TRUE COMMENT '활성 여부',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    `description` TEXT         NULL COMMENT '설명',
    `role_id`     INT          NULL COMMENT '권한 id',
    CONSTRAINT `fk_job_role`
        FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
)
    COMMENT = '직무 테이블';

CREATE TABLE `rank`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '직급 ID',
    `name`        VARCHAR(255) NOT NULL COMMENT '직급명',
    `code`        VARCHAR(255) NOT NULL COMMENT '직급 코드',
    `is_active`   BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '활성 여부',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    `salary_band` INT          NULL COMMENT '급여 밴드',
    `role_id`     INT          NULL COMMENT '권한 id',
    CONSTRAINT `fk_rank_role`
        FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
)
    COMMENT = '직급 테이블';