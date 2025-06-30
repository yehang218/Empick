package com.piveguyz.empickbackend.employment.applicant.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicantQueryDTO {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String profileUrl;
    private String birth;
    private String address;
}