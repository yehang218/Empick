CREATE TABLE `mail` (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
    `applicant_id` INT NOT NULL COMMENT '지원자 id',
    `email` VARCHAR(255) NOT NULL COMMENT '이메일',
    `content` LONGTEXT NOT NULL COMMENT '내용',
    FOREIGN KEY (`applicant_id`) REFERENCES `applicant`(`id`)
);