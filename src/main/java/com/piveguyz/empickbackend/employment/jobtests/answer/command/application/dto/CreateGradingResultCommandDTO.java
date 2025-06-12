package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@Setter
@AllArgsConstructor
@Builder
public class CreateGradingResultCommandDTO {
    private String evaluatorComment;
    private String isSatisfied;
    private int answerId;
    private int questionGradingCriteriaId;
}
