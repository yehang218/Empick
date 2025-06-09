package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobtestQuestionResponseDTO {
    private int id;
    private int score;
    private int optionNumber;

    private int jobTestId;
    private int questionId;
}
