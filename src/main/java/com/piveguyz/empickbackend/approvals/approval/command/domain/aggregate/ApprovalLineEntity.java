package com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "approval_line")
@Getter
@Setter
@NoArgsConstructor
public class ApprovalLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "step_order", nullable = false)
    private int stepOrder;

    @Column(name = "approval_category_id", nullable = false)
    private int approvalCategoryId;

    @Column(name = "position_id", nullable = false)
    private int positionId;
}
