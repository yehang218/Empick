package com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval")
@Getter
@Setter
@NoArgsConstructor
public class ApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "category_id", nullable = false)
    private int categoryId;

    @Column(name = "writer_id", nullable = false)
    private int writerId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "status", nullable = false)
    private Integer status = 0; // -1: 반려, 0: 진행중, 1: 완료

    @Column(name = "first_approver_id", nullable = true)
    private Integer firstApproverId;

    @Column(name = "second_approver_id", nullable = true)
    private Integer secondApproverId;

    @Column(name = "third_approver_id", nullable = true)
    private Integer thirdApproverId;

    @Column(name = "fourth_approver_id", nullable = true)
    private Integer fourthApproverId;

    @Column(name = "first_approved_at", nullable = true)
    private LocalDateTime firstApprovedAt;

    @Column(name = "second_approved_at", nullable = true)
    private LocalDateTime secondApprovedAt;

    @Column(name = "third_approved_at", nullable = true)
    private LocalDateTime thirdApprovedAt;

    @Column(name = "fourth_approved_at", nullable = true)
    private LocalDateTime fourthApprovedAt;

    @Column(name = "approval_id", nullable = true)
    private Integer approvalId;

    @Builder
    public ApprovalEntity(int categoryId, int writerId,
                          Integer firstApproverId, Integer secondApproverId,
                          Integer thirdApproverId, Integer fourthApproverId,
                          Integer approvalId) {
        this.categoryId = categoryId;
        this.writerId = writerId;
        this.firstApproverId = firstApproverId;
        this.secondApproverId = secondApproverId;
        this.thirdApproverId = thirdApproverId;
        this.fourthApproverId = fourthApproverId;
        this.status = 0;
        this.createdAt = LocalDateTime.now();
        this.approvalId = approvalId;
    }
}
