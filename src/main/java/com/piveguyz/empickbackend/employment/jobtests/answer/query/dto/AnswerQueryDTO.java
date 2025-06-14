package com.piveguyz.empickbackend.employment.jobtests.answer.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import com.piveguyz.empickbackend.employment.jobtests.question.query.dto.QuestionQueryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerQueryDTO {
    private int id;
    private String content;
    private int attempt;
    private CorrectType isCorrect; // 0/1/2 (ENUM)
    private Double score;
    private int applicationJobTestId;
    private int questionId;

    private int maxScore;

    // 문제 정보 포함 (QuestionQueryDTO)
    private QuestionQueryDTO question;

    // 답변의 채점 결과
    private List<GradingResultQueryDTO> gradingResults;
}
