package com.piveguyz.empickbackend.member.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<String> findRolesByEmployeeNumber(@Param("employeeNumber") int employeeNumber);
}
