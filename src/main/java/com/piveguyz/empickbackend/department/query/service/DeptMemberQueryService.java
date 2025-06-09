package com.piveguyz.empickbackend.department.query.service;

import com.piveguyz.empickbackend.department.query.dto.DeptMemberQueryResponseDTO;

import java.util.List;

public interface DeptMemberQueryService {
    List<DeptMemberQueryResponseDTO> getMembersByDepartmentId(int departmentId);
}
