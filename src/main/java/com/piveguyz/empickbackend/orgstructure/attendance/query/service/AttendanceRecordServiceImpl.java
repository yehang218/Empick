package com.piveguyz.empickbackend.orgstructure.attendance.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceRecordQueryDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.query.mapper.AttendanceRecordMapper;
import com.piveguyz.empickbackend.orgstructure.attendance.query.service.AttendanceRecordService;
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
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

    private final AttendanceRecordMapper attendanceRecordMapper;

    @Override
    public List<AttendanceRecordQueryDTO> getAllAttendanceRecords() {
        log.debug("모든 근태 기록 조회 시작");

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findAllAttendanceRecords();

        log.debug("조회된 근태 기록 수: {}", records.size());
        return records;
    }

    @Override
    public AttendanceRecordQueryDTO getAttendanceRecordById(int id) {
        log.debug("근태 기록 조회 시작 - ID: {}", id);

        AttendanceRecordQueryDTO record = attendanceRecordMapper.findById(id);

        if (record == null) {
            log.warn("근태 기록을 찾을 수 없습니다 - ID: {}", id);
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }

        log.debug("근태 기록 조회 완료 - ID: {}, MemberID: {}", id, record.getMemberId());
        return record;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getAttendanceRecordsByMemberId(int memberId) {
        log.debug("회원별 근태 기록 조회 시작 - MemberID: {}", memberId);

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByMemberId(memberId);

        log.debug("조회된 근태 기록 수 (MemberID: {}): {}", memberId, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getAttendanceRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("날짜 범위별 근태 기록 조회 시작 - Start: {}, End: {}", startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다");
        }

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByDateRange(startDate, endDate);

        log.debug("조회된 근태 기록 수 (기간: {} ~ {}): {}", startDate, endDate, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getAttendanceRecordsByMemberIdAndDateRange(int memberId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("회원별 + 날짜 범위 근태 기록 조회 시작 - MemberID: {}, Start: {}, End: {}", memberId, startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다");
        }

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByMemberIdAndDateRange(memberId, startDate, endDate);

        log.debug("조회된 근태 기록 수 (MemberID: {}, 기간: {} ~ {}): {}", memberId, startDate, endDate, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getAttendanceRecordsByCategory(int attendanceCategoryId) {
        log.debug("근태 카테고리별 기록 조회 시작 - CategoryID: {}", attendanceCategoryId);

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByAttendanceCategoryId(attendanceCategoryId);

        log.debug("조회된 근태 기록 수 (CategoryID: {}): {}", attendanceCategoryId, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getAttendanceRecordsByStatus(int status) {
        log.debug("status별 근태 기록 조회 시작 - Status: {}", status);

        // status 값 검증
        if (status < 0 || status > 2) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByStatus(status);

        log.debug("조회된 근태 기록 수 (Status: {}): {}", status, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getAllActiveAttendanceRecords() {
        log.debug("활성화된 모든 근태 기록 조회 시작");

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findAllActive();

        log.debug("조회된 활성화 근태 기록 수: {}", records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getRecentAttendanceRecordsByMemberId(int memberId, int limit) {
        log.debug("회원별 최근 근태 기록 조회 시작 - MemberID: {}, Limit: {}", memberId, limit);

        if (limit <= 0) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findRecentByMemberId(memberId, limit);

        log.debug("조회된 최근 근태 기록 수 (MemberID: {}, Limit: {}): {}", memberId, limit, records.size());
        return records;
    }

    // ========== 로그인된 사용자용 메소드들 ==========

    @Override
    public List<AttendanceRecordQueryDTO> getMyAttendanceRecords(int memberId) {
        log.debug("내 모든 근태 기록 조회 시작 - MemberID: {}", memberId);

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByMemberId(memberId);

        log.debug("조회된 내 근태 기록 수 (MemberID: {}): {}", memberId, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getMyRecentAttendanceRecords(int memberId, int limit) {
        log.debug("내 최근 근태 기록 조회 시작 - MemberID: {}, Limit: {}", memberId, limit);

        if (limit <= 0) {
            throw new IllegalArgumentException("조회할 개수는 1 이상이어야 합니다: " + limit);
        }

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findRecentByMemberId(memberId, limit);

        log.debug("조회된 내 최근 근태 기록 수 (MemberID: {}, Limit: {}): {}", memberId, limit, records.size());
        return records;
    }

    @Override
    public List<AttendanceRecordQueryDTO> getMyAttendanceRecordsByDateRange(int memberId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("내 특정 기간 근태 기록 조회 시작 - MemberID: {}, Start: {}, End: {}", memberId, startDate, endDate);

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("시작 날짜와 종료 날짜는 필수입니다");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다");
        }

        List<AttendanceRecordQueryDTO> records = attendanceRecordMapper.findByMemberIdAndDateRange(memberId, startDate, endDate);

        log.debug("조회된 내 근태 기록 수 (MemberID: {}, 기간: {} ~ {}): {}", memberId, startDate, endDate, records.size());
        return records;
    }
}