package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobtestCommandDTO {
    // 제목, 시험 시간, 유형, 난이도
    private String title;
    private JobtestDifficulty difficulty;
    private int testTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    private int createdMemberId;

    // 문제 목록
    private List<CreateJobtestQuestionCommandDTO> jobtestQuestions;
}
