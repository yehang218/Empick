package com.piveguyz.empickbackend.employment.jobtests.answer.query.dto;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class GradingResultQueryDTO {
    private int id;
    private int gradingCriteriaId;
    private boolean isSatisfied;
    private int answerId;
}
