package com.piveguyz.empickbackend.member.command.domain.aggregate;

import com.piveguyz.empickbackend.member.command.domain.enums.FieldType;
import com.piveguyz.empickbackend.member.command.domain.enums.MemberEditStatus;
import com.piveguyz.empickbackend.member.command.domain.enums.MemberTargetField;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_edit")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberEditProposalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "reviewer_id")
    private Integer reviewerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_field", nullable = false)
    private MemberTargetField targetField;

    @Column(name = "original_value", nullable = false)
    private String originalValue;

    @Column(name = "requested_value", nullable = false)
    private String requestedValue;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private MemberEditStatus status = MemberEditStatus.PENDING;

    @CreationTimestamp
    @Column(name = "requested_at", nullable = false, updatable = false)
    private LocalDateTime requestedAt;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "reject_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    /**
     * 변경 요청 승인 처리
     */
    public void approve(Integer approverId) {
        this.reviewerId = approverId;
        this.status = MemberEditStatus.APPROVED;
    }

    /**
     * 변경 요청 거절 처리
     */
    public void reject(Integer approverId, String rejectionReason) {
        this.reviewerId = approverId;
        this.status = MemberEditStatus.REJECTED;
        this.rejectionReason = rejectionReason;
    }
}
