package com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionOptionCommandDTO {
    private Integer optionNumber;
    private String content;
}
