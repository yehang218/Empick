package com.piveguyz.empickbackend.department.query.service;

import com.piveguyz.empickbackend.department.query.dto.DeptChangeHistoryQueryDTO;
import com.piveguyz.empickbackend.department.query.mapper.DeptChangeHistoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptChangeHistoryQueryServiceImpl implements DeptChangeHistoryQueryService {

    final private DeptChangeHistoryMapper deptChangeHistoryMapper;

    @Override
    public List<DeptChangeHistoryQueryDTO> getDeptChangeHistoriesByMemberId(Integer memberId) {
        return deptChangeHistoryMapper.selectDeptChangeHistoriesByMemberId(memberId);
    }
}
