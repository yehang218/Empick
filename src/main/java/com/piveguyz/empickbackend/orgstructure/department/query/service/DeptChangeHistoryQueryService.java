package com.piveguyz.empickbackend.orgstructure.department.query.service;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptChangeHistoryQueryDTO;

import java.util.List;

public interface DeptChangeHistoryQueryService {
    List<DeptChangeHistoryQueryDTO> getDeptChangeHistoriesByMemberId(Integer memberId);
}
