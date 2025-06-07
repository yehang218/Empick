package com.piveguyz.empickbackend.employment.applicant.query.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantBookmarkQueryDTO {
    private Long memberId;
    private Long applicantId;
    private String name;
    private String phone;
    private String email;
    private String profileUrl;
    private String birth;
    private String address;
}

