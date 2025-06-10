package com.piveguyz.empickbackend.department.query.mapper;

import com.piveguyz.empickbackend.department.query.dto.DeptChangeHistoryQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptChangeHistoryMapper {
    List<DeptChangeHistoryQueryDTO> selectDeptChangeHistoriesByMemberId(@Param("memberId") Integer memberId);
}
