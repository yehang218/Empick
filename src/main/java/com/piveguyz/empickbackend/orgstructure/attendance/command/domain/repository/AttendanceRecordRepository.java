package com.piveguyz.empickbackend.orgstructure.attendance.command.domain.repository;

import com.piveguyz.empickbackend.orgstructure.attendance.command.domain.aggregate.AttendanceRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecordEntity, Integer> {

    /**
     * ID와 회원 ID로 근태 기록 조회 (권한 검증용)
     */
    Optional<AttendanceRecordEntity> findByIdAndMemberId(Integer id, Integer memberId);

    /**
     * 특정 기간 내 특정 카테고리 기록 존재 여부 확인 (삭제되지 않은 것만)
     */
    boolean existsByMemberIdAndAttendanceCategoryIdAndRecordTimeBetweenAndDeletedAtIsNull(
            Integer memberId,
            Integer attendanceCategoryId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    /**
     * 회원의 특정 날짜 근태 기록 조회 (삭제되지 않은 것만)
     */
    java.util.List<AttendanceRecordEntity> findByMemberIdAndRecordTimeBetweenAndDeletedAtIsNull(
            Integer memberId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    /**
     * 회원의 모든 근태 기록 조회 (삭제되지 않은 것만)
     */
    java.util.List<AttendanceRecordEntity> findByMemberIdAndDeletedAtIsNull(Integer memberId);
}