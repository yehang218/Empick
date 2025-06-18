package com.piveguyz.empickbackend.orgstructure.attendance.query.service.impl;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordResponseQueryDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.query.mapper.AttendanceRecordResponseMapper;
import com.piveguyz.empickbackend.orgstructure.attendance.query.service.AttendanceRecordResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttendanceRecordResponseServiceImpl implements AttendanceRecordResponseService {

    private final AttendanceRecordResponseMapper attendanceRecordResponseMapper;

    @Override
    public List<AttendanceRecordResponseQueryDTO> getAllAttendanceRecordsWithDetails() {
        log.debug("모든 근태 기록 조회 시작 (상세 정보 포함)");

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findAllWithDetails();

        log.debug("조회된 근태 기록 수 (상세 정보 포함): {}", records.size());
        return records;
    }

    @Override
    public AttendanceRecordResponseQueryDTO getAttendanceRecordByIdWithDetails(int id) {
        log.debug("근태 기록 조회 시작 (상세 정보 포함) - ID: {}", id);

        AttendanceRecordResponseQueryDTO record = attendanceRecordResponseMapper.findByIdWithDetails(id);

        if (record == null) {
            log.warn("근태 기록을 찾을 수 없습니다 - ID: {}", id);
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }

        log.debug("근태 기록 조회 완료 (상세 정보 포함) - ID: {}, MemberName: {}, CategoryLabel: {}",
                id, record.getMemberName(), record.getAttendanceCategoryLabel());
        return record;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByMemberIdWithDetails(int memberId) {
        log.debug("회원별 근태 기록 조회 시작 (상세 정보 포함) - MemberID: {}", memberId);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByMemberIdWithDetails(memberId);

        log.debug("조회된 근태 기록 수 (상세 정보 포함, MemberID: {}): {}", memberId, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByDateRangeWithDetails(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("날짜 범위별 근태 기록 조회 시작 (상세 정보 포함) - Start: {}, End: {}", startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다");
        }

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByDateRangeWithDetails(startDate, endDate);

        log.debug("조회된 근태 기록 수 (상세 정보 포함, 기간: {} ~ {}): {}", startDate, endDate, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByMemberIdAndDateRangeWithDetails(int memberId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("회원별 + 날짜 범위 근태 기록 조회 시작 (상세 정보 포함) - MemberID: {}, Start: {}, End: {}", memberId, startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다");
        }

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByMemberIdAndDateRangeWithDetails(memberId, startDate, endDate);

        log.debug("조회된 근태 기록 수 (상세 정보 포함, MemberID: {}, 기간: {} ~ {}): {}", memberId, startDate, endDate, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByCategoryWithDetails(int attendanceCategoryId) {
        log.debug("근태 카테고리별 기록 조회 시작 (상세 정보 포함) - CategoryID: {}", attendanceCategoryId);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByAttendanceCategoryWithDetails(attendanceCategoryId);

        log.debug("조회된 근태 기록 수 (상세 정보 포함, CategoryID: {}): {}", attendanceCategoryId, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getRecentAttendanceRecordsByMemberIdWithDetails(int memberId, int limit) {
        log.debug("회원별 최근 근태 기록 조회 시작 (상세 정보 포함) - MemberID: {}, Limit: {}", memberId, limit);

        if (limit <= 0) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findRecentByMemberIdWithDetails(memberId, limit);

        log.debug("조회된 최근 근태 기록 수 (상세 정보 포함, MemberID: {}, Limit: {}): {}", memberId, limit, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getAttendanceRecordsByDateWithDetails(LocalDateTime date) {
        log.debug("특정 날짜 근태 기록 조회 시작 (상세 정보 포함) - Date: {}", date);

        if (date == null) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByDateWithDetails(date);

        log.debug("조회된 근태 기록 수 (상세 정보 포함, Date: {}): {}", date, records.size());
        return records;
    }

    // ========== 로그인된 사용자용 메소드들 ==========

    @Override
    public List<AttendanceRecordResponseQueryDTO> getMyAttendanceRecords(int memberId) {
        log.debug("내 모든 근태 기록 조회 시작 (상세 정보 포함) - MemberID: {}", memberId);

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByMemberIdWithDetails(memberId);

        log.debug("조회된 내 근태 기록 수 (상세 정보 포함, MemberID: {}): {}", memberId, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getMyRecentAttendanceRecords(int memberId, int limit) {
        log.debug("내 최근 근태 기록 조회 시작 (상세 정보 포함) - MemberID: {}, Limit: {}", memberId, limit);

        if (limit <= 0) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findRecentByMemberIdWithDetails(memberId, limit);

        log.debug("조회된 내 최근 근태 기록 수 (상세 정보 포함, MemberID: {}, Limit: {}): {}", memberId, limit, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordResponseQueryDTO> getMyAttendanceRecordsByDateRange(int memberId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("내 특정 기간 근태 기록 조회 시작 (상세 정보 포함) - MemberID: {}, Start: {}, End: {}", memberId, startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다");
        }

        List<AttendanceRecordResponseQueryDTO> records = attendanceRecordResponseMapper.findByMemberIdAndDateRangeWithDetails(memberId, startDate, endDate);

        log.debug("조회된 내 근태 기록 수 (상세 정보 포함, MemberID: {}, 기간: {} ~ {}): {}", memberId, startDate, endDate, records.size());
        return records;
    }
}