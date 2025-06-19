package com.piveguyz.empickbackend.orgstructure.attendance.query.mapper;

import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AttendanceRecordMapper {

    // 기본 조회
    List<AttendanceRecordQueryDTO> findAllAttendanceRecords();
    AttendanceRecordQueryDTO findById(@Param("id") int id);

    // 회원별 조회
    List<AttendanceRecordQueryDTO> findByMemberId(@Param("memberId") int memberId);

    // 날짜 범위로 조회
    List<AttendanceRecordQueryDTO> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    // 회원별 + 날짜 범위 조회
    List<AttendanceRecordQueryDTO> findByMemberIdAndDateRange(@Param("memberId") int memberId,
                                                              @Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);

    // 근태 카테고리별 조회
    List<AttendanceRecordQueryDTO> findByAttendanceCategoryId(@Param("attendanceCategoryId") int attendanceCategoryId);

    // status별 조회 (0=평시, 1=수정됨, 2=수정요청중)
    List<AttendanceRecordQueryDTO> findByStatus(@Param("status") int status);

    // 삭제되지 않은 기록만 조회
    List<AttendanceRecordQueryDTO> findAllActive();

    // 회원별 최근 기록 조회
    List<AttendanceRecordQueryDTO> findRecentByMemberId(@Param("memberId") int memberId,
                                                        @Param("limit") int limit);
}