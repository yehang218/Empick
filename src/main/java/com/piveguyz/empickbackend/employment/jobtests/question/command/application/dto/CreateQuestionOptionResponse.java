package com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionOptionResponse {
    private int optionNumber;
    private String content;
    private int questionId;
}
