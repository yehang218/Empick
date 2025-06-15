package com.piveguyz.empickbackend.approvals.approvalCategory.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approvalCategory.command.domain.aggregate.ApprovalCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalCategoryRepository extends JpaRepository<ApprovalCategoryEntity, Integer> {
}
