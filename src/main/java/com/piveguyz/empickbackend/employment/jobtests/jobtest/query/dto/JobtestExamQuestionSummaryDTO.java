package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.question.query.dto.QuestionOptionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobtestExamQuestionSummaryDTO {
    private Integer questionId;
    private String type;
    private String title;
    private Integer score;

    private List<QuestionOptionDTO> options;
}