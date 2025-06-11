package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEvaluationResultCommandDTO {
    private String evaluatorComment;
    private Double score;
}
