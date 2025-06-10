package com.piveguyz.empickbackend.orgstructure.department.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.department.command.domain.aggregate.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<DepartmentEntity, Integer> {
}
