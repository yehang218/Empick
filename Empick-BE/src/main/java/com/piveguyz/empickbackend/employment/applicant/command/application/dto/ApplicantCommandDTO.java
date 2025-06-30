package com.piveguyz.empickbackend.employment.applicant.command.application.dto;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantCommandDTO {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String profileUrl;
    private String birth;
    private String address;
}

