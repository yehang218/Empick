package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;


import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateJobtestQuestionCommandDTO {
    private Integer score;
    private Integer optionNumber;
}
