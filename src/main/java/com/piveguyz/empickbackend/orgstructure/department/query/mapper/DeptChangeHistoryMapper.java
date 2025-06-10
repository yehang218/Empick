package com.piveguyz.empickbackend.orgstructure.department.query.mapper;

import com.piveguyz.empickbackend.orgstructure.department.query.dto.DeptChangeHistoryQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptChangeHistoryMapper {
    List<DeptChangeHistoryQueryDTO> selectDeptChangeHistoriesByMemberId(@Param("memberId") Integer memberId);
}
