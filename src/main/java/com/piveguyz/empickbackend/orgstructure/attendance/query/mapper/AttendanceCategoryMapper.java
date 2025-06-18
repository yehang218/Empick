package com.piveguyz.empickbackend.orgstructure.attendance.query.mapper;

import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceCategoryQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttendanceCategoryMapper {

    // 기본 조회
    List<AttendanceCategoryQueryDTO> findAll();
    AttendanceCategoryQueryDTO findById(@Param("id") int id);

    // status별 조회 (0=출근, 1=퇴근, 2=지각, 3=조퇴)
    List<AttendanceCategoryQueryDTO> findByStatus(@Param("status") int status);

    // 복수 ID로 조회
    List<AttendanceCategoryQueryDTO> findByIds(@Param("ids") List<Integer> ids);

    // 전체 개수
    int countAll();
}