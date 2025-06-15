package com.piveguyz.empickbackend.orgstructure.department.query.mapper;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptResponseQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptMapper {
    List<DeptResponseQueryDTO> selectAllDepartments();

    DeptResponseQueryDTO selectDepartmentById(@Param("id") Integer id);

}
