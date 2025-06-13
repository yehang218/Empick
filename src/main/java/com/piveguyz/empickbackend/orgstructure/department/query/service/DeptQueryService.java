package com.piveguyz.empickbackend.orgstructure.department.query.service;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptResponseQueryDTO;

import java.util.List;

public interface DeptQueryService {

    // 전체 부서 조회
    List<DeptResponseQueryDTO> getDepartments();

    // 단일 부서 조회
    DeptResponseQueryDTO getDepartmentById(Integer id);
}