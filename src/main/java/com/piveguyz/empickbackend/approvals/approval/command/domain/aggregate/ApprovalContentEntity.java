package com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "approval_content")
public class ApprovalContentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "approval_id", nullable = false)
    private int approvalId;

    @Column(name = "item_id", nullable = false)
    private int itemId;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Builder
    public ApprovalContentEntity(Integer approvalId, Integer itemId, String content) {
        this.approvalId = approvalId;
        this.itemId = itemId;
        this.content = content;
    }
}
