package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateAnswerResponseDTO {
    private String content;
    private CorrectType isCorrect;
    private int attempt;
    private int applicationJobTestId;
    private int questionId;
}
