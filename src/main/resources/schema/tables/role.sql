DROP TABLE IF EXISTS `role`;

CREATE TABLE `role`
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '권한 테이블 ID',
    `code`        VARCHAR(255) NOT NULL COMMENT '권한코드 ex) ROLE_XXX',
    `name`        VARCHAR(255) NOT NULL COMMENT '권한이름',
    `role_type`   TINYINT      NOT NULL COMMENT '0 = 부서, 1 = 직급, 2 = 직책, 3 = 직무',
    `description` TEXT         NOT NULL COMMENT '설명',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    `deleted_at`  DATETIME     NULL COMMENT '삭제일',
    UNIQUE KEY `uk_role_code` (`code`)
)
    COMMENT = '권한 테이블';