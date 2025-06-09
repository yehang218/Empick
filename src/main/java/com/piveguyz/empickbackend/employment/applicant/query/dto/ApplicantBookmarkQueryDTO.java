package com.piveguyz.empickbackend.employment.applicant.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantBookmarkQueryDTO {
    private int memberId;
    private int applicantId;
    private String name;
    private String phone;
    private String email;
    private String profileUrl;
    private String birth;
    private String address;
}

