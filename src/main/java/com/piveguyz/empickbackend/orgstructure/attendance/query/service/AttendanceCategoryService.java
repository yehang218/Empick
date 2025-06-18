package com.piveguyz.empickbackend.orgstructure.attendance.query.service;

import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceCategoryQueryDTO;

import java.util.List;

public interface AttendanceCategoryService {

    /**
     * 모든 근태 카테고리 조회
     * @return 근태 카테고리 목록
     */
    List<AttendanceCategoryQueryDTO> getAllAttendanceCategories();

    /**
     * ID로 근태 카테고리 조회
     * @param id 근태 카테고리 ID
     * @return 근태 카테고리 정보
     */
    AttendanceCategoryQueryDTO getAttendanceCategoryById(int id);

    /**
     * status별 근태 카테고리 조회
     * @param status 카테고리 상태 (0=출근, 1=퇴근, 2=지각, 3=조퇴)
     * @return 해당 status의 근태 카테고리 목록
     */
    List<AttendanceCategoryQueryDTO> getAttendanceCategoriesByStatus(int status);

    /**
     * 복수 ID로 근태 카테고리 조회
     * @param ids 근태 카테고리 ID 목록
     * @return 근태 카테고리 목록
     */
    List<AttendanceCategoryQueryDTO> getAttendanceCategoriesByIds(List<Integer> ids);

    /**
     * 전체 근태 카테고리 개수 조회
     * @return 전체 개수
     */
    int getTotalCount();
}