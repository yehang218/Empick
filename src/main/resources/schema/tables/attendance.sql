-- 🗂️ 근태 카테고리
CREATE TABLE `attendance_category`
(
    `id`         INT      NOT NULL AUTO_INCREMENT COMMENT '근태 카테고리 id',
    `status`     TINYINT  NOT NULL COMMENT '카테고리 종류: 0=출근, 1=퇴근, 2=지각, 3=조퇴',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
    PRIMARY KEY (`id`)
);

-- 📝 근태 기록
CREATE TABLE `attendance_record`
(
    `id`                     INT      NOT NULL AUTO_INCREMENT COMMENT '근태 기록 id',
    `member_id`              INT      NOT NULL COMMENT '회원 ID',
    `attendance_category_id` INT      NOT NULL COMMENT '근태 카테고리 ID',
    `record_time`            DATETIME NOT NULL COMMENT '기록 시각',
    `status`                 TINYINT  NOT NULL DEFAULT 0 COMMENT '0=평시, 1=수정됨, 2=수정요청중',
    `created_at`             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    `updated_at`             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
    `deleted_at`             DATETIME          DEFAULT NULL COMMENT '삭제 시각 (소프트 삭제)',
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_record_time` (`record_time`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_attendance_record_member` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
        ON DELETE RESTRICT,
    CONSTRAINT `fk_attendance_record_category` FOREIGN KEY (`attendance_category_id`) REFERENCES `attendance_category` (`id`)
);
