package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAnswerCommandDTO {
    private CorrectType isCorrect;
    private Double score;
}
