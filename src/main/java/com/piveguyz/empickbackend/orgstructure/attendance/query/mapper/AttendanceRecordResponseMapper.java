package com.piveguyz.empickbackend.orgstructure.attendance.query.mapper;

import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordResponseQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AttendanceRecordResponseMapper {

    // 기본 조회 (조인된 정보 포함)
    List<AttendanceRecordResponseQueryDTO> findAllWithDetails();
    AttendanceRecordResponseQueryDTO findByIdWithDetails(@Param("id") int id);

    // 회원별 조회 (회원명 포함)
    List<AttendanceRecordResponseQueryDTO> findByMemberIdWithDetails(@Param("memberId") int memberId);

    // 날짜 범위로 조회 (모든 상세 정보 포함)
    List<AttendanceRecordResponseQueryDTO> findByDateRangeWithDetails(@Param("startDate") LocalDateTime startDate,
                                                                      @Param("endDate") LocalDateTime endDate);

    // 회원별 + 날짜 범위 조회
    List<AttendanceRecordResponseQueryDTO> findByMemberIdAndDateRangeWithDetails(@Param("memberId") int memberId,
                                                                                 @Param("startDate") LocalDateTime startDate,
                                                                                 @Param("endDate") LocalDateTime endDate);

    // 근태 카테고리별 조회 (라벨 포함)
    List<AttendanceRecordResponseQueryDTO> findByAttendanceCategoryWithDetails(@Param("attendanceCategoryId") int attendanceCategoryId);

    // 회원별 최근 기록 (상세 정보 포함)
    List<AttendanceRecordResponseQueryDTO> findRecentByMemberIdWithDetails(@Param("memberId") int memberId,
                                                                           @Param("limit") int limit);

    // 특정 날짜의 모든 기록 (상세 정보 포함)
    List<AttendanceRecordResponseQueryDTO> findByDateWithDetails(@Param("date") LocalDateTime date);
}