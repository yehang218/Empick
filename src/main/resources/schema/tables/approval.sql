SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `approval_content`;
DROP TABLE IF EXISTS `approval`;
DROP TABLE IF EXISTS `approval_category_item`;
DROP TABLE IF EXISTS `approval_category`;

SET foreign_key_checks = 1;

-- 결재 문서 유형
CREATE TABLE `approval_category` (
    `id`                   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `approval_category_id` INT          NULL     COMMENT '상위 카테고리 id',
    `name`                 VARCHAR(255) NOT NULL COMMENT '결재 문서 유형 이름'
);

-- 결재 유형별 항목
CREATE TABLE `approval_category_item` (
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_id` INT          NOT NULL,
    `name`        VARCHAR(255) NOT NULL COMMENT '항목 이름',
    input_type  tinyint      not null comment '입력 타입 (0: TEXT, 1: TEXTAREA, 2: FILE 등)',

    FOREIGN KEY (`category_id`) REFERENCES `approval_category` (`id`) ON DELETE CASCADE
);

-- 결재 문서
CREATE TABLE `approval` (
    `id`                 INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `category_id`        INT      NOT NULL,
    `writer_id`          INT      NOT NULL COMMENT '기안자 id',
    `created_at`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `status`             TINYINT  NOT NULL DEFAULT 0 COMMENT '결재 상태 (-2: 취소, -1: 반려, 0: 진행중, 1: 완료)',
    `first_approver_id`  INT      NULL,
    `second_approver_id` INT      NULL,
    `third_approver_id`  INT      NULL,
    `fourth_approver_id` INT      NULL,
    `first_approved_at`  DATETIME NULL,
    `second_approved_at` DATETIME NULL,
    `third_approved_at`  DATETIME NULL,
    `fourth_approved_at` DATETIME NULL,
    `approval_id`          INT NULL             COMMENT '취소 대상 결재 id',

    FOREIGN KEY (`category_id`) REFERENCES `approval_category` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`writer_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`first_approver_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`second_approver_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`third_approver_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`fourth_approver_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
);

-- 결재 문서 내용
CREATE TABLE `approval_content` (
    `id`          INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `approval_id` INT      NOT NULL,
    `item_id`     INT      NOT NULL,
    `content`     LONGTEXT NOT NULL COMMENT '입력 내용',

    FOREIGN KEY (`approval_id`) REFERENCES `approval` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`item_id`) REFERENCES `approval_category_item` (`id`) ON DELETE CASCADE
);
