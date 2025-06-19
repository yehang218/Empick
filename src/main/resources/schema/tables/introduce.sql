create table introduce
(
    id                    int auto_increment
        primary key,
    content               varchar(255) not null,
    applicant_id          int          not null,
    introduce_template_id int          not null,
    application_id        int          not null,
    constraint fk_introduce_applicant
        foreign key (applicant_id) references applicant (id),
    constraint fk_introduce_application
        foreign key (application_id) references application (id),
    constraint introduce_ibfk_2
        foreign key (introduce_template_id) references introduce_template (id)
);

create table introduce_template
(
    id        int auto_increment
        primary key,
    title     varchar(255) not null,
    member_id int          not null,
    constraint introduce_template_ibfk_1
        foreign key (member_id) references member (id)
);

create table introduce_template_item
(
    id                    int auto_increment
        primary key,
    title                 varchar(255) not null,
    member_id             int          not null,
    introduce_template_id int          null
);

create table introduce_template_item_response
(
    id                         int auto_increment comment '응답 ID'
        primary key,
    introduce_id               int      not null comment '자기소개서 ID',
    introduce_template_item_id int      not null comment '자기소개서 템플릿 항목 ID',
    content                    longtext not null comment '지원자 응답 내용',
    constraint introduce_template_item_response_ibfk_1
        foreign key (introduce_id) references introduce (id),
    constraint introduce_template_item_response_ibfk_2
        foreign key (introduce_template_item_id) references introduce_template_item (id)
);

create table introduce_standard_item
(
    id                    int auto_increment
        primary key,
    content               varchar(255) not null,
    member_id             int          not null,
    introduce_standard_id int          not null,
    constraint introduce_standard_item_ibfk_1
        foreign key (member_id) references member (id),
    foreign key (introduce_standard_id) references  introduce_standard(id)

);

create table introduce_standard
(
    id           int auto_increment
        primary key,
    content      varchar(255) not null,
    member_id    int          not null,
    introduce_id int          not null,
    constraint introduce_standard_ibfk_1
        foreign key (member_id) references member (id),
    constraint introduce_standard_ibfk_2
        foreign key (introduce_id) references introduce (id)
);

create table introduce_rating_result
(
    id                    int auto_increment
        primary key,
    member_id             int      not null,
    content               longtext not null,
    rating_score          int      not null,
    introduce_standard_id int      not null,
    updated_by            int      null,
    updated_at            datetime null,
    constraint introduce_rating_result_ibfk_1
        foreign key (member_id) references member (id),
    constraint introduce_rating_result_ibfk_2
        foreign key (introduce_standard_id) references introduce_standard (id),
    constraint introduce_rating_result_ibfk_3
        foreign key (updated_by) references member (id)
