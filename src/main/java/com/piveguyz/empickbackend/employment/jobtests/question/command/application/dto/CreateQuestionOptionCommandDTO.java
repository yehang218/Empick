package com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class CreateQuestionOptionCommandDTO {
    private String content;
    private int questionId;

    @Builder
    public CreateQuestionOptionCommandDTO(int optionNumber, String content, int questionId) {
        this.content = content;
        this.questionId = questionId;
    }
}
