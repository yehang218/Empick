CREATE TABLE `dept_change_history`
(
    `id`            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '부서이동내역 id',
    `member_id`     INT          NOT NULL COMMENT '사원 id',
    `dept_name`     VARCHAR(255) NOT NULL COMMENT '부서명',
    `position_name` VARCHAR(255) NULL COMMENT '직책',
    `job_name`      VARCHAR(255) NULL COMMENT '직무',
    `rank_name`     VARCHAR(255) NOT NULL COMMENT '직급',
    `work_start_at` DATETIME     NOT NULL COMMENT '근무 시작일',
    `work_end_at`   DATETIME     NULL COMMENT '근무 종료일',
    CONSTRAINT `fk_dept_change_history_member`
        FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
)
    COMMENT = '부서 이동 내역';