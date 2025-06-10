package com.piveguyz.empickbackend.orgstructure.department.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.department.command.domain.aggregate.DeptChangeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptChangeHistoryRepository extends JpaRepository<DeptChangeHistoryEntity, Integer> {
}
