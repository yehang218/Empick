package com.piveguyz.empickbackend.approvals.approval.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<ApprovalEntity, Integer> {
}
