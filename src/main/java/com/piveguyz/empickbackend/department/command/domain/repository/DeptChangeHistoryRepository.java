package com.piveguyz.empickbackend.department.command.domain.repository;

import com.piveguyz.empickbackend.department.command.domain.aggregate.DeptChangeHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptChangeHistoryRepository extends JpaRepository<DeptChangeHistoryEntity, Integer> {
}
