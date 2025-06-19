DROP TABLE IF EXISTS introduce_template_item;
DROP TABLE IF EXISTS introduce_template;
DROP TABLE IF EXISTS introduce;
DROP TABLE IF EXISTS introduce_standard_item;
DROP TABLE IF EXISTS introduce_standard;
DROP TABLE IF EXISTS introduce_rating_result;


-- introduce_template_item
create table introduce_template_item
(
    id                    int auto_increment
        primary key,
    title                 varchar(255) not null,
    member_id             int          not null,
    introduce_template_id int          null
);

create index fk_introduce_template
    on introduce_template_item (introduce_template_id);

create index member_id
    on introduce_template_item (member_id);

-- introduce_template
create table introduce_template
(
    id        int auto_increment
        primary key,
    title     varchar(255) not null,
    member_id int          not null,
    constraint introduce_template_ibfk_1
        foreign key (member_id) references member (id)
);

create index member_id
    on introduce_template (member_id);



create table introduce_standard_item
(
    id                    int auto_increment
        primary key,
    content               varchar(255) not null,
    member_id             int          not null,
    introduce_standard_id int          not null,
    constraint introduce_standard_item_ibfk_1
        foreign key (member_id) references member (id)
);

create index member_id
    on introduce_standard_item (member_id);



-- auto-generated definition
create table introduce
(
    id                    int auto_increment
        primary key,
    content               varchar(255) not null,
    member_id             int          not null,
    introduce_template_id int          not null,
    updated_by            int          null,
    updated_at            datetime     null,
    constraint introduce_ibfk_1
        foreign key (member_id) references member (id),
    constraint introduce_ibfk_2
        foreign key (introduce_template_id) references introduce_template (id),
    constraint introduce_ibfk_3
        foreign key (updated_by) references member (id)
);

create index introduce_template_id
    on introduce (introduce_template_id);

create index member_id
    on introduce (member_id);

create index updated_by
    on introduce (updated_by);



-- auto-generated definition
create table introduce_template
(
    id        int auto_increment
        primary key,
    title     varchar(255) not null,
    member_id int          not null,
    constraint introduce_template_ibfk_1
        foreign key (member_id) references member (id)
);

create index member_id
    on introduce_template (member_id);



-- auto-generated definition
create table introduce_standard_item
(
    id                    int auto_increment
        primary key,
    content               varchar(255) not null,
    member_id             int          not null,
    introduce_standard_id int          not null,
    constraint introduce_standard_item_ibfk_1
        foreign key (member_id) references member (id)
);

create index member_id
    on introduce_standard_item (member_id);



-- auto-generated definition
create table introduce_rating_result
(
    id                    int auto_increment
        primary key,
    member_id             int        not null,
    content               longtext   not null,
    rating_score          int        not null,
    introduce_standard_id int        not null,
    status                tinyint(1) not null comment '확인=1, 탈락=0, 보류=2',
    updated_by            int        null,
    updated_at            datetime   null,
    constraint introduce_rating_result_ibfk_1
        foreign key (member_id) references member (id),
    constraint introduce_rating_result_ibfk_2
        foreign key (introduce_standard_id) references introduce_standard (id),
    constraint introduce_rating_result_ibfk_3
        foreign key (updated_by) references member (id)
);

create index introduce_standard_id
    on introduce_rating_result (introduce_standard_id);

create index member_id
    on introduce_rating_result (member_id);

create index updated_by
    on introduce_rating_result (updated_by);

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

create index introduce_id
    on introduce_template_item_response (introduce_id);

create index introduce_template_item_id
    on introduce_template_item_response (introduce_template_item_id);


