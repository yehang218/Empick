package com.piveguyz.empickbackend.approvals.approval.command.domain.repository;

import com.piveguyz.empickbackend.approvals.approval.command.domain.aggregate.ApprovalLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalLineRepository extends JpaRepository<ApprovalLineEntity, Integer> {
    List<ApprovalLineEntity> findByApprovalCategoryIdOrderByStepOrderAsc(Integer categoryId);
}
