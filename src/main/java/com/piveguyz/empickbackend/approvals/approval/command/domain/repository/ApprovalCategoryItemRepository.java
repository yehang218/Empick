package com.piveguyz.empickbackend.approvals.approval.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalCategoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalCategoryItemRepository extends JpaRepository<ApprovalCategoryItemEntity, Integer> {
}
