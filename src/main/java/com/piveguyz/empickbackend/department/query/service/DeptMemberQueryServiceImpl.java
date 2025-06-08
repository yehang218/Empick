package com.piveguyz.empickbackend.department.query.service;

import com.piveguyz.empickbackend.department.query.dto.DeptMemberQueryResponseDTO;
import com.piveguyz.empickbackend.department.query.mapper.DeptMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptMemberQueryServiceImpl implements DeptMemberQueryService {

    private final DeptMemberMapper deptMemberMapper;

    @Override
    public List<DeptMemberQueryResponseDTO> getMembersByDepartmentId(int departmentId) {
        return deptMemberMapper.findMembersByDepartmentId(departmentId);
    }
}
