package com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateGradingCriteriaCommandDTO {
    private String content;
    private String detailContent;
    private Double scoreWeight;
}
