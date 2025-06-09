package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto;

import lombok.*;

// 각 채점 기준에 따른 평가 결과 DTO

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class EvaluationResultQueryDTO {
    private String criteriaContent;         // 내용 (job_test_evaluation_criteria.content)
    private String criteriaDetailContent;   // 상세 내용 (job_test_evaluation_criteria.detail_content)
    private Double criteriaScoreWeight;     // 가중치 (job_test_evaluation_criteria.score_weight)

    private String resultEvaluatorComment;  // 평가자 코멘트 (job_test_evaluation_result.evaluator_comment)
    private Integer resultScore;            // 평가 점수 (job_test_evaluation_result.score)
}
