package com.piveguyz.empickbackend.orgstructure.department.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.department.command.application.dto.DeptCommandDTO;
import jakarta.validation.Valid;

public interface DeptCommandService {

    DeptCommandDTO createDept(@Valid DeptCommandDTO deptCommandDTO);

    DeptCommandDTO updateDept(int id, @Valid DeptCommandDTO deptCommandDTO);

    Integer updateDeptActiveStatus(int id, IsActive isActive);

}