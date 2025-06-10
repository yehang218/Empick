package com.piveguyz.empickbackend.orgstructure.member.query.mapper;

import com.piveguyz.empickbackend.orgstructure.member.query.dto.MemberResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<String> findRolesByEmployeeNumber(@Param("employeeNumber") int employeeNumber);

    MemberResponseDTO findMemberById(int memberId);

    List<MemberResponseDTO> findMembersByName(@Param("name") String name);

    List<MemberResponseDTO> findMembersByEmployeeNumber(@Param("employeeNumber") int employeeNumber);
}
