package com.piveguyz.empickbackend.approvals.approval.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalContentRepository extends JpaRepository<ApprovalContentEntity, Integer> {
}
