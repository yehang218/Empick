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

-- 🔁 근태 기록 수정 요청
CREATE TABLE `attendance_record_change_request`
(
    `id`                   INT      NOT NULL AUTO_INCREMENT COMMENT '수정 요청 번호',
    `attendance_record_id` INT      NOT NULL COMMENT '수정 대상 근태 기록 ID',
    `reason`               LONGTEXT NOT NULL COMMENT '수정 요청 사유',
    `request_at`           DATETIME NOT NULL COMMENT '요청 시각',
    `status`               TINYINT  NOT NULL DEFAULT 0 COMMENT '0=요청, 1=승인, 2=반려',
    `approved_member_id`   INT      NOT NULL COMMENT '승인자 ID',  -- ✅ NOT NULL + RESTRICT 적용
    `approved_at`          DATETIME NULL COMMENT '승인 시각',
    `created_at`           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 시각',
    `updated_at`           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 시각',
    PRIMARY KEY (`id`),
    KEY `idx_change_status` (`status`),
    CONSTRAINT `fk_change_request_record` FOREIGN KEY (`attendance_record_id`) REFERENCES `attendance_record` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_change_request_approver` FOREIGN KEY (`approved_member_id`) REFERENCES `member` (`id`)
        ON DELETE RESTRICT
);
