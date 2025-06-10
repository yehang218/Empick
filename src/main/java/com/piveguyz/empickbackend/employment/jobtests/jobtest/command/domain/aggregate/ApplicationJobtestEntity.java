package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.enums.JobtestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "application_job_test")
public class ApplicationJobtestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "evaluator_comment", nullable = false)
    private String evaluatorComment;

    @Column(name = "submitted_at", nullable = true)
    private LocalDateTime submittedAt;

    @Column(name = "grading_total_score", nullable = true)
    private Integer gradingTotalScore;

    @Column(name = "evaluation_score", nullable = true)
    private Integer evaluationScore;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "grading_status", nullable = false)
    private JobtestStatus gradingStatus;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "evaluation_status", nullable = false)
    private JobtestStatus evaluationStatus;

    @Column(name = "entry_code", nullable = true)
    private String entryCode;

    @Column(name = "application_id", nullable = false)
    private int applicationId;

    @Column(name = "job_test_id", nullable = false)
    private int jobTestId;

    @Column(name = "member_id", nullable = true)
    private Integer memberId;


    public void updateApplicationJobtestEntity(UpdateApplicationJobtestCommandDTO updateApplicationJobtestCommandDTO) {
        if(updateApplicationJobtestCommandDTO.getEvaluatorComment() != null) {
            this.evaluatorComment = updateApplicationJobtestCommandDTO.getEvaluatorComment();
        }
        if(updateApplicationJobtestCommandDTO.getSubmittedAt() != null) {
            this.submittedAt = updateApplicationJobtestCommandDTO.getSubmittedAt();
        }
        if(updateApplicationJobtestCommandDTO.getGradingTotalScore() != null) {
            this.gradingTotalScore = updateApplicationJobtestCommandDTO.getGradingTotalScore();
        }
        if(updateApplicationJobtestCommandDTO.getEvaluationScore() != null) {
            this.evaluationScore = updateApplicationJobtestCommandDTO.getEvaluationScore();
        }
        if(updateApplicationJobtestCommandDTO.getGradingStatus() != null) {
            this.gradingStatus = updateApplicationJobtestCommandDTO.getGradingStatus();
        }
        if(updateApplicationJobtestCommandDTO.getEvaluationStatus() != null) {
            this.evaluationStatus = updateApplicationJobtestCommandDTO.getEvaluationStatus();
        }
        if(updateApplicationJobtestCommandDTO.getEntryCode() != null) {
            this.entryCode = updateApplicationJobtestCommandDTO.getEntryCode();
        }
        if(updateApplicationJobtestCommandDTO.getMemberId() != null) {
            this.memberId = updateApplicationJobtestCommandDTO.getMemberId();
        }

    }
}
