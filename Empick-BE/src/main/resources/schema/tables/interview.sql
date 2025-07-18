SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `interview_sheet`;
DROP TABLE IF EXISTS `interview_criteria`;
DROP TABLE IF EXISTS `interview`;
DROP TABLE IF EXISTS `interviewer`;
DROP TABLE IF EXISTS `interview_score`;

SET foreign_key_checks = 1;

CREATE TABLE `interview_sheet` (
   `id`	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
   `name`	VARCHAR(255)	NOT NULL COMMENT '이름',
   `is_deleted`	VARCHAR(4)	NOT NULL	DEFAULT 'N' COMMENT '삭제 여부',
   `member_id`	INT NOT NULL COMMENT '수정자 id',
   `updated_at`	DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
   FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
)
COMMENT = '면접 평가표';

CREATE TABLE `interview_criteria` (
  `id`	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
  `sheet_id` INT NOT NULL COMMENT '평가표 id',
  `title`	VARCHAR(255)	NOT NULL COMMENT '제목',
  `content`	LONGTEXT	NOT NULL COMMENT '내용',
    `weight` DOUBLE NOT NULL COMMENT '가중치',
  `is_deleted`	VARCHAR(4)	NOT NULL	DEFAULT 'N' COMMENT '삭제 여부',
  `member_id`	INT	NOT NULL COMMENT '수정자 id',
  `updated_at`	DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    FOREIGN KEY (`sheet_id`) REFERENCES `interview_sheet`(`id`),
  FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
)
COMMENT = '면접 평가표 기준';


CREATE TABLE `interview` (
     `id`	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
     `application_id` INT NOT NULL COMMENT '지원서 id',
     `sheet_id`	INT	NOT NULL COMMENT '평가표 id',
     `datetime`	DATETIME	NOT NULL COMMENT '면접 일시',
     `address`	LONGTEXT	NOT NULL COMMENT '주소',
     `score`	DOUBLE	NULL COMMENT '점수',
     FOREIGN KEY (`application_id`) REFERENCES `application`(`id`),
     FOREIGN KEY (`sheet_id`) REFERENCES `interview_sheet`(`id`)
)
COMMENT = '면접';

CREATE TABLE `interviewer` (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
    `interview_id` INT NOT NULL COMMENT '면접 id',
    `interviewer_id` INT NOT NULL COMMENT '면접 담당자 id',
    `score` DOUBLE NULL COMMENT '합산 점수',
    `review` LONGTEXT NULL COMMENT '총 평가',
    FOREIGN KEY (`interview_id`) REFERENCES `interview`(`id`),
    FOREIGN KEY (`interviewer_id`) REFERENCES `member`(`id`)
)
COMMENT '면접 담당자';

CREATE TABLE `interview_score` (
   `id`	INT	NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'id',
   `interviewer_id`	INT	NOT NULL COMMENT '면접 담당자 id',
   `criteria_id`	INT	NOT NULL COMMENT '평가 기준 id',
   `score`	INT	NULL COMMENT '점수',
   `review`	LONGTEXT    NULL COMMENT '평가',
   FOREIGN KEY (`interviewer_id`) REFERENCES `interviewer`(`id`),
   FOREIGN KEY (`criteria_id`) REFERENCES `interview_criteria`(`id`)
)
COMMENT = '면접 평가 점수';