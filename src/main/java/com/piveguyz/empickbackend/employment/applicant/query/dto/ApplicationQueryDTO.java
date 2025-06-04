package com.piveguyz.empickbackend.employment.applicant.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationQueryDTO {
    private long id;
    private long recruitmentId;
    private LocalDateTime createdAt;
    private int status; // 또는 enum으로 선언해도 좋음
    private long applicantId;
    private Long introduceRatingResultId; // nullable
    private Long jobTestEvaluationResultId; // nullable
    private Long interviewId; // nullable
    private LocalDateTime updatedAt; // DATETIME 타입은 LocalDateTime이 적절
    private Long updatedBy; // 수정자 member_id

}
