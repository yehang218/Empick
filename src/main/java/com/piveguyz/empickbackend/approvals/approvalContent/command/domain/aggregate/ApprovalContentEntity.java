package com.piveguyz.empickbackend.approvals.approvalContent.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="approval_content")
public class ApprovalContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "approval_id")
    private Integer approvalId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "content")
    private String content;
}
