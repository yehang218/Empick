package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobtestQuestionCommandDTO {
    private int score;
    private int questionId;
}
