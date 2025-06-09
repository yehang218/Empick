package com.piveguyz.empickbackend.department.query.mapper;

import com.piveguyz.empickbackend.department.query.dto.DeptMemberQueryResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMemberMapper {

    /**
     * 부서 ID로 해당 부서의 사원 리스트를 조회합니다.
     *
     * @param departmentId 조회할 부서 ID
     * @return 부서에 속한 사원의 리스트
     */
    List<DeptMemberQueryResponseDTO> findMembersByDepartmentId(@Param("departmentId") int departmentId);
}