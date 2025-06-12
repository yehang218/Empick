DROP TABLE IF EXISTS introduce_template_item;
DROP TABLE IF EXISTS introduce_template;
DROP TABLE IF EXISTS introduce;
DROP TABLE IF EXISTS introduce_standard_item;
DROP TABLE IF EXISTS introduce_standard;
DROP TABLE IF EXISTS introduce_rating_result;


-- introduce_template_item
CREATE TABLE introduce_template_item
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    introduce_template_id INT NOT NULL,
    FOREIGN KEY (introduce_template_id) REFERENCES introduce_template(id),
    FOREIGN KEY (member_id) REFERENCES member(id)
);

-- introduce_template
CREATE TABLE introduce_template
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id)
);

-- introduce_standard_item
CREATE TABLE introduce
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    introduce_template_id INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (introduce_template_id) REFERENCES introduce_template(id),
);

-- introduce
CREATE TABLE introduce_standard
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    introduce_id INT NOT NULL,
    introduce_standard_item_id INT NOT NULL,
    updated_by INT NULL,
    updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (introduce_id) REFERENCES introduce(id),
    FOREIGN KEY (introduce_standard_item_id) REFERENCES introduce_standard_item(id),
    FOREIGN KEY (updated_by) REFERENCES member(id)
);

-- introduce_standard
CREATE TABLE introduce_standard_item
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    member_id INT NOT NULL,
    introduce_standard_id INT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (introduce_standard_id) REFERENCES introduce_standard(id)
);

-- introduce_rating_result
CREATE TABLE introduce_rating_result
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id INT NOT NULL,
    content LONGTEXT NOT NULL,
    rating_score INT NOT NULL,
    introduce_standard_id INT NOT NULL,
    status TINYINT(1) NOT NULL COMMENT '확인=1, 탈락=0, 보류=2',
    updated_by INT NULL,
    updated_at DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES member(id),
    FOREIGN KEY (introduce_standard_id) REFERENCES introduce_standard(id),
    FOREIGN KEY (updated_by) REFERENCES member(id)
);

