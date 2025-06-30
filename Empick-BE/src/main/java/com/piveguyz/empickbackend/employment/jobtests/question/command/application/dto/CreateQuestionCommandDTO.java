package com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.CreateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.enums.QuestionType;
import lombok.*;

import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionCommandDTO {
    private String content;
    private String detailContent;
    private QuestionType type;
    private JobtestDifficulty difficulty;
    private String answer;

    private int createdMemberId;

    private List<CreateQuestionOptionCommandDTO> questionOptions;       // 선택지
    private List<CreateGradingCriteriaCommandDTO> gradingCriteria;      // 채점 기준
}
