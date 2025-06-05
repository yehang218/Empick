package com.piveguyz.empickbackend.employment.jobtests.jobtest.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobtestQuestionQueryDTO {
    private int id;
    private int score;
    private int optionNumber;
    private int questionId;
}
