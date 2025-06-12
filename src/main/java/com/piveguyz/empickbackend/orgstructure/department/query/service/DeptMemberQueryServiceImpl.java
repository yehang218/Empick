package com.piveguyz.empickbackend.orgstructure.department.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptMemberQueryResponseDTO;
import com.piveguyz.empickbackend.orgstructure.department.query.mapper.DeptMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptMemberQueryServiceImpl implements DeptMemberQueryService {

    private final DeptMemberMapper deptMemberMapper;

    @Override
    public List<DeptMemberQueryResponseDTO> getMembersByDepartmentId(int departmentId) {
        List<DeptMemberQueryResponseDTO> members = deptMemberMapper.findMembersByDepartmentId(departmentId);
        if (members.isEmpty()) {
            throw new BusinessException(ResponseCode.MEMBER_NOT_FOUND); // 사원이 없을 때 예외 처리
        }
        return members;
    }
}
