package com.piveguyz.empickbackend.approvals.approval.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Integer> {
}
