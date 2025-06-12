package com.piveguyz.empickbackend.employment.applicant.command.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantBookmarkCommandDTO {
    private int memberId;
    private int applicantId;
}


