package com.piveguyz.empickbackend.employment.jobtest.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums.JobtestDifficulty;
import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums.JobtestType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class CreateJobTestQuestionCommandDTO {
    private String content;
    private String detailContent;
    private JobtestType type;
    private JobtestDifficulty difficulty;
    private String answer;

    private int createdMemberId;
}
