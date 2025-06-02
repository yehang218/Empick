DROP TABLE IF EXISTS application_item;
DROP TABLE IF EXISTS application_item_category;
DROP TABLE IF EXISTS recruitment_template_copy;
DROP TABLE IF EXISTS recruitment_template_item;
DROP TABLE IF EXISTS recruitment_process;
DROP TABLE IF EXISTS recruitment;
DROP TABLE IF EXISTS recruitment_template;
DROP TABLE IF EXISTS recruitment_request;

-- 채용요청서
CREATE TABLE IF NOT EXISTS recruitment_request (
    id INT NOT NULL AUTO_INCREMENT,
    headcount INT NOT NULL,
    started_at DATETIME NOT NULL,
    ended_at DATETIME NOT NULL,
    qualification VARCHAR(255),
    preference VARCHAR(255),
    responsibilities VARCHAR(255),
    employment_type VARCHAR(255) NOT NULL,
    work_location VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    department_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_recruitment_request PRIMARY KEY (id),
    CONSTRAINT fk_recruitment_request_member_id FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_recruitment_request_department_id FOREIGN KEY (department_id) REFERENCES department(id)
);


-- 채용공고 템플릿
CREATE TABLE IF NOT EXISTS recruitment_template (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted VARCHAR(4) NOT NULL DEFAULT 'N',
    member_id INT NOT NULL,

    CONSTRAINT pk_recruitment_template PRIMARY KEY (id),
    CONSTRAINT fk_recruitment_template_member_id FOREIGN KEY (member_id) REFERENCES member(id)
);

-- 채용공고
CREATE TABLE IF NOT EXISTS recruitment (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content LONGTEXT,
    recruit_type TINYINT NOT NULL,
    status TINYINT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    started_at DATETIME,
    ended_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    member_id INT NOT NULL,
    recruitment_template_id INT,
#     introduce_template_id INT NOT NULL,
    recruitment_request_id INT UNIQUE,

    CONSTRAINT pk_recruitment PRIMARY KEY (id),
    CONSTRAINT fk_recruitment_member_id FOREIGN KEY (member_id) REFERENCES member(id),
    CONSTRAINT fk_recruitment_template_id FOREIGN KEY (recruitment_template_id) REFERENCES recruitment_template(id),
#     CONSTRAINT fk_introduce_template_id FOREIGN KEY (introduce_template_id) REFERENCES introduce_template(id),
    CONSTRAINT fk_recruitment_request_id FOREIGN KEY (recruitment_request_id) REFERENCES recruitment_request(id)
);

-- 채용공고별 채용 프로세스
CREATE TABLE IF NOT EXISTS recruitment_process (
    id INT NOT NULL AUTO_INCREMENT,
    step_type TINYINT NOT NULL,
    display_order INT NOT NULL,
    recruitment_id INT NOT NULL,

    CONSTRAINT pk_recruitment_process PRIMARY KEY (id),
    CONSTRAINT fk_recruitment_process_recruitment_id FOREIGN KEY (recruitment_id) REFERENCES recruitment(id)
);

-- 채용 공고 템플릿 항목
CREATE TABLE IF NOT EXISTS recruitment_template_item (
    id INT NOT NULL AUTO_INCREMENT,
    item_title VARCHAR(255) NOT NULL,
    default_content LONGTEXT NOT NULL,
    display_order INT NOT NULL,
    recruitment_template_id INT NOT NULL,

    CONSTRAINT pk_recruitment_template_item PRIMARY KEY (id),
    CONSTRAINT fk_recruitment_template_item_template_id FOREIGN KEY (recruitment_template_id) REFERENCES recruitment_template(id),
    CONSTRAINT uq_recruitment_template_item UNIQUE (recruitment_template_id, display_order)
);

-- 채용 공고 템플릿 항목 복사본
CREATE TABLE IF NOT EXISTS recruitment_template_copy (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    display_order INT NOT NULL,
    recruitment_id INT NOT NULL,
    recruitment_template_item_id INT NOT NULL,

    CONSTRAINT pk_recruitment_template_copy PRIMARY KEY (id),
    CONSTRAINT fk_template_copy_recruitment_id FOREIGN KEY (recruitment_id) REFERENCES recruitment(id),
    CONSTRAINT fk_template_copy_item_id FOREIGN KEY (recruitment_template_item_id) REFERENCES recruitment_template_item(id),
    CONSTRAINT uq_recruitment_template_copy UNIQUE (recruitment_id, display_order)
);

-- 지원서 항목 카테고리
CREATE TABLE IF NOT EXISTS application_item_category (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    input_type TINYINT NOT NULL,
    display_order INT NOT NULL,

    CONSTRAINT pk_application_item_category PRIMARY KEY (id)
);

-- 지원서 항목
CREATE TABLE IF NOT EXISTS application_item (
    id INT NOT NULL AUTO_INCREMENT,
    is_required VARCHAR(4) NOT NULL,
    application_item_category_id INT NOT NULL,
    recruitment_id INT NOT NULL,

    CONSTRAINT pk_application_item PRIMARY KEY (id),
    CONSTRAINT fk_application_item_category_id FOREIGN KEY (application_item_category_id) REFERENCES application_item_category(id),
    CONSTRAINT fk_application_item_recruitment_id FOREIGN KEY (recruitment_id) REFERENCES recruitment(id),
    CONSTRAINT uq_application_item UNIQUE (recruitment_id, application_item_category_id)
);
