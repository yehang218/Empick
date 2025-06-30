package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateGradingResultCommandDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "grading_result")
public class GradingResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "evaluator_comment", nullable = false)
    private String evaluatorComment;

    @Column(name = "is_satisfied", nullable = false)
    private String isSatisfied;

    public boolean isSatisfied() {
        return "Y".equalsIgnoreCase(isSatisfied);
    }

    @Column(name = "answer_id", nullable = false)
    private int answerId;

    @Column(name = "question_grading_criteria_id", nullable = false)
    private int questionGradingCriteriaId;

    public void updateGradingCriteria(UpdateGradingResultCommandDTO dto) {
        if(dto.getEvaluatorComment() != null) {
            this.evaluatorComment = dto.getEvaluatorComment();
        }
        if(dto.getIsSatisfied() != null) {
            this.isSatisfied = dto.getIsSatisfied();
        }
    }
}
