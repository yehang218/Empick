package com.piveguyz.empickbackend.orgstructure.attendance.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_record")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "attendance_category_id", nullable = false)
    private Integer attendanceCategoryId;

    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private Integer status = 0;  // 0=평시, 1=수정됨, 2=수정요청중

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;  // soft delete

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}