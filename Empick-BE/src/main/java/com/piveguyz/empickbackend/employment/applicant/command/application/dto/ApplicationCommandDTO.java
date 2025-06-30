package com.piveguyz.empickbackend.employment.applicant.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCommandDTO {
    private Integer id;
    private int recruitmentId;
    private int applicantId;
    private int status;
    private Integer introduceRatingResultId;
}