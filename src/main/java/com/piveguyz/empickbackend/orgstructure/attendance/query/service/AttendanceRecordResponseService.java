package com.piveguyz.empickbackend.orgstructure.attendance.query.service;

import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordResponseQueryDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRecordResponseService {

    /**
     * 모든 근태 기록 조회 (상세 정보 포함)
     * @return 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getAllAttendanceRecordsWithDetails();

    /**
     * ID로 근태 기록 조회 (상세 정보 포함)
     * @param id 근태 기록 ID
     * @return 상세 정보가 포함된 근태 기록
     */
    AttendanceRecordResponseQueryDTO getAttendanceRecordByIdWithDetails(int id);

    /**
     * 회원별 근태 기록 조회 (상세 정보 포함)
     * @param memberId 회원 ID
     * @return 해당 회원의 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByMemberIdWithDetails(int memberId);

    /**
     * 날짜 범위로 근태 기록 조회 (상세 정보 포함)
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간의 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByDateRangeWithDetails(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 회원별 + 날짜 범위 근태 기록 조회 (상세 정보 포함)
     * @param memberId 회원 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 회원의 해당 기간 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByMemberIdAndDateRangeWithDetails(int memberId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 근태 카테고리별 기록 조회 (상세 정보 포함)
     * @param attendanceCategoryId 근태 카테고리 ID
     * @return 해당 카테고리의 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByCategoryWithDetails(int attendanceCategoryId);

    /**
     * 회원별 최근 근태 기록 조회 (상세 정보 포함)
     * @param memberId 회원 ID
     * @param limit 조회할 개수
     * @return 해당 회원의 최근 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getRecentAttendanceRecordsByMemberIdWithDetails(int memberId, int limit);

    /**
     * 특정 날짜의 모든 근태 기록 조회 (상세 정보 포함)
     * @param date 조회할 날짜
     * @return 해당 날짜의 상세 정보가 포함된 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByDateWithDetails(LocalDateTime date);

    // ========== 로그인된 사용자용 메소드들 ==========

    /**
     * 내 모든 근태 기록 조회
     * @param memberId 로그인된 사용자 ID
     * @return 내 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getMyAttendanceRecords(int memberId);

    /**
     * 내 최근 근태 기록 조회
     * @param memberId 로그인된 사용자 ID
     * @param limit 조회할 개수
     * @return 내 최근 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getMyRecentAttendanceRecords(int memberId, int limit);

    /**
     * 내 특정 기간 근태 기록 조회
     * @param memberId 로그인된 사용자 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 내 해당 기간 근태 기록 목록
     */
    List<AttendanceRecordResponseQueryDTO> getMyAttendanceRecordsByDateRange(int memberId, LocalDateTime startDate, LocalDateTime endDate);
}