package com.piveguyz.empickbackend.orgstructure.department.query.service;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptMemberQueryResponseDTO;

import java.util.List;

public interface DeptMemberQueryService {
    List<DeptMemberQueryResponseDTO> getMembersByDepartmentId(int departmentId);
}
