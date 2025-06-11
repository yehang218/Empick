package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAnswerCommandDTO {
    private String content;

    private int applicationJobTestId;
    private int questionId;
}
