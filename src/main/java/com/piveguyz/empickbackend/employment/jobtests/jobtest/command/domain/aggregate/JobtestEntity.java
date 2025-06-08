package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestCommandDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "job_test")
public class JobtestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "difficulty", nullable = false)
    private JobtestDifficulty difficulty;

    @Column(name = "test_time", nullable = false)
    private int testTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "created_member_id", nullable = false)
    private int createdMemberId;

    @Column(name = "updated_member_id", nullable = true)
    private Integer updatedMemberId;

    @PrePersist
    public void PrePersist() {this.createdAt = LocalDateTime.now(); }

    @PreUpdate
    public void PreUpdate() {this.updatedAt = LocalDateTime.now(); }

    public void updateJobtestEntity(UpdateJobtestCommandDTO updateJobtestCommandDTO) {
        if(updateJobtestCommandDTO.getTitle() != null) {
            this.title = updateJobtestCommandDTO.getTitle();
        }
        if(updateJobtestCommandDTO.getTitle() != null) {
            this.difficulty = updateJobtestCommandDTO.getDifficulty();
        }
        if(updateJobtestCommandDTO.getTestTime() != null) {
            this.testTime = updateJobtestCommandDTO.getTestTime();
        }

        this.updatedMemberId = updateJobtestCommandDTO.getUpdatedMemberId();
        this.updatedAt = LocalDateTime.now();
    }
}
