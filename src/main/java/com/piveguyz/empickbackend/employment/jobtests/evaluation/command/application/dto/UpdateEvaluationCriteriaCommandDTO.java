package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEvaluationCriteriaCommandDTO {
    private String content;
    private String detailContent;
    private Double scoreWeight;
}
