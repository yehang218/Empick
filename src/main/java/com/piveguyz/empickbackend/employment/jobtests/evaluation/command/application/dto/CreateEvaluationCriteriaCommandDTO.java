package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEvaluationCriteriaCommandDTO {
    private String content;
    private String detailContent;
    private double scoreWeight;

    private int jobtestId;
}
