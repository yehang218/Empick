SET foreign_key_checks = 0;

DROP TABLE IF EXISTS `job_test_evaluation_result`;
DROP TABLE IF EXISTS `job_test_evaluation_criteria`;
DROP TABLE IF EXISTS `grading_result`;
DROP TABLE IF EXISTS `question_grading_criteria`;
DROP TABLE IF EXISTS `answer`;
DROP TABLE IF EXISTS `job_test_question`;
DROP TABLE IF EXISTS `question_option`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `application_job_test`;
DROP TABLE IF EXISTS `job_test`;

SET foreign_key_checks = 1;

# ========================== 실무 테스트
CREATE TABLE job_test
(
    id                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(255) NOT NULL,
    difficulty        TINYINT      NOT NULL,
    test_time         INT          NOT NULL,
    started_at        DATETIME     NULL COMMENT '시험 시작 일시',
    ended_at          DATETIME     NULL COMMENT '시험 종료 일시',
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     NULL,
    created_member_id INT          NOT NULL,
    updated_member_id INT          NULL,

    FOREIGN KEY (`created_member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY (`updated_member_id`) REFERENCES `member` (`id`)
);


# ========================== 지원서별 실무 테스트
CREATE TABLE application_job_test
(
    id                   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    evaluator_comment    LONGTEXT     NULL COMMENT '평가자 코멘트',
    submitted_at         DATETIME     NULL COMMENT '제출 일시',
    grading_total_score  DOUBLE       NOT NULL DEFAULT 0.0 COMMENT '채점 총 점수',
    evaluation_score     DOUBLE       NOT NULL DEFAULT 0.0 COMMENT '평가 점수',
    grading_status       TINYINT      NOT NULL DEFAULT 0 COMMENT '채점 상태',
    evaluation_status    TINYINT      NOT NULL DEFAULT 0 COMMENT '평가 상태',
    entry_code           VARCHAR(255) NULL COMMENT '입장 코드',

    application_id       INT          NOT NULL COMMENT '지원서 id',
    job_test_id          INT          NOT NULL COMMENT '실무테스트 id',
    grading_member_id    INT          NULL COMMENT '채점자 id',
    evaluation_member_id INT          NULL COMMENT '평가자 id',

    FOREIGN KEY (`application_id`) REFERENCES `application` (`id`),
    FOREIGN KEY (`job_test_id`) REFERENCES `job_test` (`id`),
    FOREIGN KEY (`grading_member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY (`evaluation_member_id`) REFERENCES `member` (`id`)
);


# ========================== 실무 테스트 문제
CREATE TABLE question
(
    id                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content           LONGTEXT     NOT NULL,
    detail_content    LONGTEXT     NULL,
    type              TINYINT      NOT NULL,
    difficulty        TINYINT      NOT NULL,
    answer            VARCHAR(255) NULL,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     NULL,
    created_member_id INT          NOT NULL,
    updated_member_id INT          NULL,

    FOREIGN KEY (`created_member_id`) REFERENCES `member` (`id`),
    FOREIGN KEY (`updated_member_id`) REFERENCES `member` (`id`)
);

# ========================== 실무 테스트 문제별 선택지
CREATE TABLE question_option
(
    id            INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    option_number INT          NOT NULL,
    content       VARCHAR(255) NOT NULL,
    question_id   INT          NOT NULL,

    FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);


# ========================== 실무 테스트별 문제
CREATE TABLE job_test_question
(
    id            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    score         INT NOT NULL,
    option_number INT NOT NULL,
    job_test_id   INT NOT NULL,
    question_id   INT NOT NULL,

    FOREIGN KEY (`job_test_id`) REFERENCES `job_test` (`id`),
    FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);


# ========================== 지원서별 문제 답변
CREATE TABLE answer
(
    id                      INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content                 LONGTEXT NOT NULL,
    attempt                 INT      NOT NULL DEFAULT 1,
    is_correct              TINYINT  NULL,
    score                   DOUBLE   NULL DEFAULT 0.0,
    application_job_test_id INT      NOT NULL,
    question_id             INT      NOT NULL,

    FOREIGN KEY (`application_job_test_id`) REFERENCES `application_job_test` (`id`),
    FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);


# ========================== 실무 테스트 문제별 채점 기준
CREATE TABLE question_grading_criteria
(
    id             INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content        LONGTEXT NOT NULL,
    detail_content LONGTEXT NULL,
    score_weight   DOUBLE   NOT NULL,
    question_id    INT      NOT NULL,

    FOREIGN KEY (`question_id`) REFERENCES `question` (`id`)
);


# ========================== 실무 테스트 답안 채점 결과
CREATE TABLE grading_result
(
    id                           INT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    evaluator_comment            LONGTEXT   NULL,
    is_satisfied                 VARCHAR(4) NOT NULL,
    answer_id                    INT        NOT NULL,
    question_grading_criteria_id INT        NOT NULL,

    FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`),
    FOREIGN KEY (`question_grading_criteria_id`) REFERENCES `question_grading_criteria` (`id`)
);


# ========================== 실무 테스트 평가 기준
CREATE TABLE job_test_evaluation_criteria
(
    id             INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content        LONGTEXT NOT NULL,
    detail_content LONGTEXT NOT NULL,
    score_weight   DOUBLE   NOT NULL,
    job_test_id    INT      NOT NULL,

    FOREIGN KEY (`job_test_id`) REFERENCES `job_test` (`id`)
);


# ========================== 실무 테스트 평가 결과
CREATE TABLE job_test_evaluation_result
(
    id                              INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    evaluator_comment               LONGTEXT NULL,
    score                           DOUBLE   NOT NULL DEFAULT 0.0,
    application_job_test_id         INT      NOT NULL,
    job_test_evaluation_criteria_id INT      NOT NULL,

    FOREIGN KEY (`application_job_test_id`) REFERENCES `application_job_test` (`id`),
    FOREIGN KEY (`job_test_evaluation_criteria_id`) REFERENCES `job_test_evaluation_criteria` (`id`)
);
