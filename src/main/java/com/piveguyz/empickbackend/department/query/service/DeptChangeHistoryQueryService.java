package com.piveguyz.empickbackend.department.query.service;

import com.piveguyz.empickbackend.department.query.dto.DeptChangeHistoryQueryDTO;

import java.util.List;

public interface DeptChangeHistoryQueryService {
    List<DeptChangeHistoryQueryDTO> getDeptChangeHistoriesByMemberId(Integer memberId);
}
