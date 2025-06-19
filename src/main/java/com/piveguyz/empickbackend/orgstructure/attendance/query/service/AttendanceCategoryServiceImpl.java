package com.piveguyz.empickbackend.orgstructure.attendance.query.service.impl;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.attendance.query.dto.AttendanceCategoryQueryDTO;
import com.piveguyz.empickbackend.orgstructure.attendance.query.mapper.AttendanceCategoryMapper;
import com.piveguyz.empickbackend.orgstructure.attendance.query.service.AttendanceCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttendanceCategoryServiceImpl implements AttendanceCategoryService {

    private final AttendanceCategoryMapper attendanceCategoryMapper;

    @Override
    public List<AttendanceCategoryQueryDTO> getAllAttendanceCategories() {
        log.debug("모든 근태 카테고리 조회 시작");

        List<AttendanceCategoryQueryDTO> categories = attendanceCategoryMapper.findAll();

        log.debug("조회된 근태 카테고리 수: {}", categories.size());
        return categories;
    }

    @Override
    public AttendanceCategoryQueryDTO getAttendanceCategoryById(int id) {
        log.debug("근태 카테고리 조회 시작 - ID: {}", id);

        AttendanceCategoryQueryDTO category = attendanceCategoryMapper.findById(id);

        if (category == null) {
            log.warn("근태 카테고리를 찾을 수 없습니다 - ID: {}", id);
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }

        log.debug("근태 카테고리 조회 완료 - ID: {}, Status: {}", id, category.getStatus());
        return category;
    }

    @Override
    public List<AttendanceCategoryQueryDTO> getAttendanceCategoriesByStatus(int status) {
        log.debug("status별 근태 카테고리 조회 시작 - Status: {}", status);

        // status 값 검증
        if (status < 0 || status > 3) {
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceCategoryQueryDTO> categories = attendanceCategoryMapper.findByStatus(status);

        log.debug("조회된 근태 카테고리 수 (Status: {}): {}", status, categories.size());
        return categories;
    }

    @Override
    public List<AttendanceCategoryQueryDTO> getAttendanceCategoriesByIds(List<Integer> ids) {
        log.debug("복수 ID로 근태 카테고리 조회 시작 - IDs: {}", ids);

        if (ids == null || ids.isEmpty()) {
            log.warn("조회할 ID 목록이 비어있습니다");
            throw new BusinessException(ResponseCode.BAD_REQUEST);
        }

        List<AttendanceCategoryQueryDTO> categories = attendanceCategoryMapper.findByIds(ids);

        log.debug("조회된 근태 카테고리 수: {}", categories.size());
        return categories;
    }

    @Override
    public int getTotalCount() {
        log.debug("전체 근태 카테고리 개수 조회 시작");

        int count = attendanceCategoryMapper.countAll();

        log.debug("전체 근태 카테고리 개수: {}", count);
        return count;
    }
}