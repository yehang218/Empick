package com.piveguyz.empickbackend.orgstructure.department.query.service;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptResponseQueryDTO;
import com.piveguyz.empickbackend.orgstructure.department.query.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeptQueryServiceImpl implements DeptQueryService {

    private final DeptMapper deptMapper;

    @Override
    public List<DeptResponseQueryDTO> getDepartments() {
        return deptMapper.selectAllDepartments();
    }

    @Override
    public DeptResponseQueryDTO getDepartmentById(Integer id) {
        DeptResponseQueryDTO dto = deptMapper.selectDepartmentById(id);
        if (dto == null) {
            throw new IllegalArgumentException("부서를 찾을 수 없습니다.");
        }
        return dto;
    }
}