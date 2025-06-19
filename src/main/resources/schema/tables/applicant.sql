SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS application_response;
DROP TABLE IF EXISTS applicant;
DROP TABLE IF EXISTS application;
DROP TABLE IF EXISTS applicant_bookmark;
SET FOREIGN_KEY_CHECKS = 1;

create table applicant
(
    id          int auto_increment comment '지원자 ID'
        primary key,
    name        varchar(255) not null comment '이름',
    phone       varchar(255) not null comment '연락처',
    email       varchar(255) not null comment '이메일',
    profile_url varchar(255) null,
    birth       varchar(255) not null comment '생년월일',
    address     varchar(255) not null comment '주소',
    constraint uq_applicant_email
        unique (email),
    constraint uq_applicant_phone
        unique (phone)
)
    comment '지원자 테이블';


-- auto-generated definition
create table application
(
    id                         int auto_increment comment '지원서 번호'
        primary key,
    recruitment_id             int      not null comment '채용 공고 id',
    created_at                 datetime not null comment '작성일자',
    status                     tinyint  not null comment '처리상태: 0(서류제외대기중), 1(서류합격), 2(실무합격), 3(1차면접합격), 4(2차면접합격), 5(최종합격), 6(불합격)',
    applicant_id               int      not null comment '지원자 id',
    introduce_rating_result_id int      null comment '자기소개서 평가 결과 id',
    updated_at                 datetime null on update current_timestamp() comment '수정시각',
    updated_by                 int      null comment '수정자 (member_id)',
    constraint application_ibfk_1
        foreign key (recruitment_id) references recruitment (id),
    constraint application_ibfk_2
        foreign key (applicant_id) references applicant (id),
    constraint application_ibfk_3
        foreign key (introduce_rating_result_id) references introduce_rating_result (id),
    constraint fk_application_updated_by
        foreign key (updated_by) references member (id)
);

create index applicant_id
    on application (applicant_id);

create index introduce_rating_result_id
    on application (introduce_rating_result_id);

create index recruitment_id
    on application (recruitment_id);




-- auto-generated definition
create table application_response
(
    id                  int auto_increment comment '지원서 항목별 내용 번호'
        primary key,
    application_id      int      not null comment '지원서 번호',
    application_item_id int      not null comment '공고별 지원서 항목 번호',
    content             longtext not null comment '지원자가 작성한 내용',
    constraint application_response_ibfk_1
        foreign key (application_id) references application (id),
    constraint application_response_ibfk_2
        foreign key (application_item_id) references application_item (id)
);

create index application_id
    on application_response (application_id);

create index application_item_id
    on application_response (application_item_id);


-- auto-generated definition
create table applicant_bookmark
(
    member_id    int not null comment '사용자 ID',
    applicant_id int not null comment '지원자 ID',
    primary key (member_id, applicant_id),
    constraint applicant_bookmark_ibfk_1
        foreign key (member_id) references member (id),
    constraint applicant_bookmark_ibfk_2
        foreign key (applicant_id) references applicant (id)
)
    comment '북마크 지원자 테이블';

create index applicant_id
    on applicant_bookmark (applicant_id);

