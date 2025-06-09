package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateGradingResultCommandDTO {
    private String evaluatorComment;
    private String isSatisfied;
}
