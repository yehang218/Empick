package com.piveguyz.empickbackend.department.command.domain.repository;

import com.piveguyz.empickbackend.department.command.domain.aggregate.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {
}
