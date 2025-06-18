package com.piveguyz.empickbackend.orgstructure.attendance.query.service;

import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordQueryDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRecordService {

    /**
     * 모든 근태 기록 조회
     * @return 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAllAttendanceRecords();

    /**
     * ID로 근태 기록 조회
     * @param id 근태 기록 ID
     * @return 근태 기록 정보
     */
    AttendanceRecordQueryDTO getAttendanceRecordById(int id);

    /**
     * 회원별 근태 기록 조회
     * @param memberId 회원 ID
     * @return 해당 회원의 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAttendanceRecordsByMemberId(int memberId);

    /**
     * 날짜 범위로 근태 기록 조회
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAttendanceRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 회원별 + 날짜 범위 근태 기록 조회
     * @param memberId 회원 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 회원의 해당 기간 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAttendanceRecordsByMemberIdAndDateRange(int memberId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 근태 카테고리별 기록 조회
     * @param attendanceCategoryId 근태 카테고리 ID
     * @return 해당 카테고리의 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAttendanceRecordsByCategory(int attendanceCategoryId);

    /**
     * status별 근태 기록 조회
     * @param status 기록 상태 (0=평시, 1=수정됨, 2=수정요청중)
     * @return 해당 status의 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAttendanceRecordsByStatus(int status);

    /**
     * 삭제되지 않은 모든 근태 기록 조회
     * @return 활성화된 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getAllActiveAttendanceRecords();

    /**
     * 회원별 최근 근태 기록 조회
     * @param memberId 회원 ID
     * @param limit 조회할 개수
     * @return 해당 회원의 최근 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getRecentAttendanceRecordsByMemberId(int memberId, int limit);

    // ========== 로그인된 사용자용 메소드들 ==========

    /**
     * 내 모든 근태 기록 조회
     * @param memberId 로그인된 사용자 ID
     * @return 내 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getMyAttendanceRecords(int memberId);

    /**
     * 내 최근 근태 기록 조회
     * @param memberId 로그인된 사용자 ID
     * @param limit 조회할 개수
     * @return 내 최근 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getMyRecentAttendanceRecords(int memberId, int limit);

    /**
     * 내 특정 기간 근태 기록 조회
     * @param memberId 로그인된 사용자 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 내 해당 기간 근태 기록 목록
     */
    List<AttendanceRecordQueryDTO> getMyAttendanceRecordsByDateRange(int memberId, LocalDateTime startDate, LocalDateTime endDate);
}