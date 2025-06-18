package com.piveguyz.empickbackend.orgstructure.attendance.command.application.service;

import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordCreateRequestDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordCreateResponseDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordUpdateRequestDTO;

public interface AttendanceRecordCommandService {

    /**
     * 근태 기록 생성
     * @param memberId 회원 ID (JWT에서 추출)
     * @param requestDTO 근태 기록 생성 요청 정보
     * @return 생성된 근태 기록 정보
     */
    AttendanceRecordCreateResponseDTO createAttendanceRecord(Integer memberId, AttendanceRecordCreateRequestDTO requestDTO);

    /**
     * 근태 기록 수정
     * @param recordId 수정할 근태 기록 ID
     * @param memberId 회원 ID (권한 검증용)
     * @param requestDTO 수정 요청 정보
     * @return 수정된 근태 기록 정보
     */
    AttendanceRecordCreateResponseDTO updateAttendanceRecord(Integer recordId, Integer memberId, AttendanceRecordUpdateRequestDTO requestDTO);

    /**
     * 근태 기록 삭제 (소프트 삭제)
     * @param recordId 삭제할 근태 기록 ID
     * @param memberId 회원 ID (권한 검증용)
     */
    void deleteAttendanceRecord(Integer recordId, Integer memberId);

    /**
     * 오늘 출근 기록 존재 여부 확인
     * @param memberId 회원 ID
     * @return 출근 기록 존재 여부
     */
    boolean hasCheckInToday(Integer memberId);

    /**
     * 오늘 퇴근 기록 존재 여부 확인
     * @param memberId 회원 ID
     * @return 퇴근 기록 존재 여부
     */
    boolean hasCheckOutToday(Integer memberId);
}