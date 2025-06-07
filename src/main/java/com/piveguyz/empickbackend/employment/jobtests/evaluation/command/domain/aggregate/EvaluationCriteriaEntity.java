package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationCriteriaCommandDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "job_test_evaluation_criteria")
public class EvaluationCriteriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "detail_content", nullable = true)
    private String detailContent;

    @Column(name = "score_weight", nullable = false)
    private double scoreWeight;

    @Column(name = "job_test_id", nullable = false)
    private int jobTestId;


    public void updateEvaluationCriteria(UpdateEvaluationCriteriaCommandDTO dto) {
        if(dto.getContent() != null) {
            this.content = dto.getContent();
        }

        if(dto.getDetailContent() != null) {
            this.detailContent = dto.getDetailContent();
        }

        if(dto.getScoreWeight() != null) {
            this.scoreWeight = dto.getScoreWeight();
        }
    }

}
