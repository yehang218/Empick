package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto;

import lombok.*;

import java.util.List;

// 지원서별 실무 테스트 종합 평가 정보 DTO

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class JobTestEvaluationDetailDTO {
    private String evaluationComment;    // 총 코멘트 (application_job_test.evaluator_comment)
    private Double evaluationScore;     // 최종 평가 점수 (application_job_test.evaluation_score)
    private Double gradingTotalScore;   // 채점 총점 (application_job_test.grading_total_score)
    private String evaluatorName;        // 평가자 이름 (member.name)

    private List<EvaluationResultQueryDTO> evaluationResults; // 각 기준별 결과 목록
}
