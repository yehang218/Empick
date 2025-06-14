package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobtestResponseDTO {
    private String title;
    private JobtestDifficulty difficulty;
    private int testTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private int createdMemberId;

    private List<CreateJobtestQuestionResponseDTO> jobtestQuestions;
}
