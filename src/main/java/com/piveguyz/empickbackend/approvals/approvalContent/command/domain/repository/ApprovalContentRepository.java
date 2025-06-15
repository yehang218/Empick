package com.piveguyz.empickbackend.approvals.approvalContent.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approvalContent.command.domain.aggregate.ApprovalContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalContentRepository extends JpaRepository<ApprovalContentEntity, Integer> {
}
