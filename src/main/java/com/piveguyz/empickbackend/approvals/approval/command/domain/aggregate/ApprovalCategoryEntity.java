package com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "approval_category")
@Getter
@Setter
@NoArgsConstructor
public class ApprovalCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "approval_category_id", nullable = true)
    private Integer parentCategoryId;

    @Column(nullable = false)
    private String name;
}
