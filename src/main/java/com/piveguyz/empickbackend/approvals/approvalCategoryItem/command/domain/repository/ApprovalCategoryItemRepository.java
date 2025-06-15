package com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approvalCategoryItem.command.domain.aggregate.ApprovalCategoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalCategoryItemRepository extends JpaRepository<ApprovalCategoryItemEntity, Integer> {
}
