package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationResultCommandDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "job_test_evaluation_result")
public class EvaluationResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "evaluator_comment", nullable = true)
    private String evaluatorComment;

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "application_job_test_id", nullable = false)
    private int applicationJobtestId;

    @Column(name = "job_test_evaluation_criteria_id", nullable = false)
    private int jobtestEvaluationCriteriaId;


    public void updateEvaluationResult(UpdateEvaluationResultCommandDTO dto) {
        if(dto.getEvaluatorComment() != null) {
            this.evaluatorComment = dto.getEvaluatorComment();
        }

        if(dto.getScore() != null) {
            this.score = dto.getScore();
        }
    }
}
