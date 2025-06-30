package com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateQuestionOptionCommandDTO {
    private String content;
}
