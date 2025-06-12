package com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.UpdateGradingCriteriaCommandDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "question_grading_criteria")
public class GradingCriteriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "detail_content", nullable = false)
    private String detailContent;

    @Column(name = "score_weight", nullable = false)
    private double scoreWeight;


    @Column(name = "question_id", nullable = false)
    private int questionId;


    public void updateGradingCriteria(UpdateGradingCriteriaCommandDTO dto) {
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
