package com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="approval")
public class ApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "writer_id")
    private Integer writerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status")
    private Integer status;

    @Column(name = "approver1_id")
    private Integer approver1Id;

    @Column(name = "approver2_id")
    private Integer approver2Id;

    @Column(name = "approver3_id")
    private Integer approver3Id;

    @Column(name = "approver4_id")
    private Integer approver4Id;

    @Column(name = "approved_at1")
    private LocalDateTime approvedAt1;

    @Column(name = "approved_at2")
    private LocalDateTime approvedAt2;

    @Column(name = "approved_at3")
    private LocalDateTime approvedAt3;

    @Column(name = "approved_at4")
    private LocalDateTime approvedAt4;
}
