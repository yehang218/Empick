package com.piveguyz.empickbackend.orgstructure.attendance.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordCreateRequestDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordCreateResponseDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.application.dto.AttendanceRecordUpdateRequestDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.command.domain.aggregate.AttendanceRecordEntity;
import com.piveguyz.empickbackend.orgstructure.attendance.command.domain.repository.AttendanceRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceRecordCommandServiceImpl implements AttendanceRecordCommandService {

    private final AttendanceRecordRepository attendanceRecordRepository;

    @Override
    public AttendanceRecordCreateResponseDTO createAttendanceRecord(Integer memberId, AttendanceRecordCreateRequestDTO requestDTO) {
        log.info("Creating attendance record for member: {}, category: {}", memberId, requestDTO.getAttendanceCategoryId());

        // 기록 시각 설정 (null이면 현재 시각 사용)
        LocalDateTime recordTime = requestDTO.getRecordTime() != null
                ? requestDTO.getRecordTime()
                : LocalDateTime.now();

        // 중복 체크 (출근/퇴근)
        validateDuplicateRecord(memberId, requestDTO.getAttendanceCategoryId(), recordTime.toLocalDate());

        // 엔티티 생성
        AttendanceRecordEntity entity = AttendanceRecordEntity.builder()
                .memberId(memberId)
                .attendanceCategoryId(requestDTO.getAttendanceCategoryId())
                .recordTime(recordTime)
                .status(0) // 평시
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 저장
        AttendanceRecordEntity savedEntity = attendanceRecordRepository.save(entity);

        log.info("Attendance record created successfully with ID: {}", savedEntity.getId());

        return AttendanceRecordCreateResponseDTO.builder()
                .id(savedEntity.getId())
                .memberId(savedEntity.getMemberId())
                .attendanceCategoryId(savedEntity.getAttendanceCategoryId())
                .recordTime(savedEntity.getRecordTime())
                .status(savedEntity.getStatus())
                .createdAt(savedEntity.getCreatedAt())
                .build();
    }

    @Override
    public AttendanceRecordCreateResponseDTO updateAttendanceRecord(Integer recordId, Integer memberId, AttendanceRecordUpdateRequestDTO requestDTO) {
        log.info("Updating attendance record ID: {} for member: {}", recordId, memberId);

        // 기존 기록 조회 및 권한 검증
        AttendanceRecordEntity entity = attendanceRecordRepository.findByIdAndMemberId(recordId, memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));

        // 삭제된 기록 체크
        if (entity.getDeletedAt() != null) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        // 수정 정보 업데이트
        if (requestDTO.getAttendanceCategoryId() != null) {
            entity.setAttendanceCategoryId(requestDTO.getAttendanceCategoryId());
        }
        if (requestDTO.getRecordTime() != null) {
            entity.setRecordTime(requestDTO.getRecordTime());
        }

        // 상태를 '수정됨'으로 변경
        entity.setStatus(1);
        entity.setUpdatedAt(LocalDateTime.now());

        // 저장
        AttendanceRecordEntity updatedEntity = attendanceRecordRepository.save(entity);

        log.info("Attendance record updated successfully: {}", recordId);

        return AttendanceRecordCreateResponseDTO.builder()
                .id(updatedEntity.getId())
                .memberId(updatedEntity.getMemberId())
                .attendanceCategoryId(updatedEntity.getAttendanceCategoryId())
                .recordTime(updatedEntity.getRecordTime())
                .status(updatedEntity.getStatus())
                .createdAt(updatedEntity.getCreatedAt())
                .build();
    }

    @Override
    public void deleteAttendanceRecord(Integer recordId, Integer memberId) {
        log.info("Deleting attendance record ID: {} for member: {}", recordId, memberId);

        // 기존 기록 조회 및 권한 검증
        AttendanceRecordEntity entity = attendanceRecordRepository.findByIdAndMemberId(recordId, memberId)
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));

        // 이미 삭제된 기록 체크
        if (entity.getDeletedAt() != null) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        // 소프트 삭제
        entity.setDeletedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        attendanceRecordRepository.save(entity);

        log.info("Attendance record deleted successfully: {}", recordId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCheckInToday(Integer memberId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return attendanceRecordRepository.existsByMemberIdAndAttendanceCategoryIdAndRecordTimeBetweenAndDeletedAtIsNull(
                memberId, 0, startOfDay, endOfDay); // 0 = 출근
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasCheckOutToday(Integer memberId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return attendanceRecordRepository.existsByMemberIdAndAttendanceCategoryIdAndRecordTimeBetweenAndDeletedAtIsNull(
                memberId, 1, startOfDay, endOfDay); // 1 = 퇴근
    }

    /**
     * 중복 기록 검증 (출근/퇴근만 체크)
     */
    private void validateDuplicateRecord(Integer memberId, Integer categoryId, LocalDate recordDate) {
        // 출근(0) 또는 퇴근(1)만 중복 체크
        if (categoryId == 0 || categoryId == 1) {
            LocalDateTime startOfDay = recordDate.atStartOfDay();
            LocalDateTime endOfDay = recordDate.atTime(LocalTime.MAX);

            boolean exists = attendanceRecordRepository.existsByMemberIdAndAttendanceCategoryIdAndRecordTimeBetweenAndDeletedAtIsNull(
                    memberId, categoryId, startOfDay, endOfDay);

            if (exists) {
                String categoryName = categoryId == 0 ? "출근" : "퇴근";
                throw new BusinessException(ResponseCode.BAD_REQUEST);
            }
        }
    }
}