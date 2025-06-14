package com.piveguyz.empickbackend.orgstructure.job.query.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import com.piveguyz.empickbackend.orgstructure.job.query.mapper.JobMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 직무 조회 서비스 구현체
 * MyBatis 기반 복잡한 조회 로직 처리 및 CustomApiResponse 반환
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobQueryServiceImpl implements JobQueryService {

    private final JobMapper jobMapper;

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getAllJobs() {
        log.debug("모든 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.selectAllJobs();
            log.debug("모든 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("모든 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsByActiveStatus(IsActive isActive) {
        log.debug("활성 여부별 직무 조회 시작. isActive: {}", isActive);
        try {
            Integer activeValue = isActive.getValue();
            List<JobResponseDTO> jobs = jobMapper.findByIsActive(activeValue);
            log.debug("활성 여부별 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("활성 여부별 직무 조회 중 오류 발생. isActive: {}", isActive, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getActiveJobs() {
        log.debug("활성 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findActiveJobs();
            log.debug("활성 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("활성 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getInactiveJobs() {
        log.debug("비활성 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findInactiveJobs();
            log.debug("비활성 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("비활성 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<JobResponseDTO> getJobById(Integer id) {
        log.debug("ID로 직무 조회 시작. id: {}", id);

        if (id == null || id <= 0) {
            log.warn("잘못된 직무 ID: {}", id);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            JobResponseDTO job = jobMapper.findById(id);
            if (job == null) {
                log.debug("ID로 직무 조회 결과 없음. id: {}", id);
                throw new BusinessException(ResponseCode.JOB_NOT_FOUND);
            }

            log.debug("ID로 직무 조회 완료. id: {}, name: {}", id, job.getName());
            return CustomApiResponse.of(ResponseCode.SUCCESS, job);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("ID로 직무 조회 중 오류 발생. id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<JobResponseDTO> getJobByCode(String code) {
        log.debug("코드로 직무 조회 시작. code: {}", code);

        if (!StringUtils.hasText(code)) {
            log.warn("잘못된 직무 코드: {}", code);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            JobResponseDTO job = jobMapper.findByCode(code.trim());
            if (job == null) {
                log.debug("코드로 직무 조회 결과 없음. code: {}", code);
                throw new BusinessException(ResponseCode.JOB_NOT_FOUND);
            }

            log.debug("코드로 직무 조회 완료. code: {}, name: {}", code, job.getName());
            return CustomApiResponse.of(ResponseCode.SUCCESS, job);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("코드로 직무 조회 중 오류 발생. code: {}", code, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<JobResponseDTO> getJobByName(String name) {
        log.debug("이름으로 직무 조회 시작. name: {}", name);

        if (!StringUtils.hasText(name)) {
            log.warn("잘못된 직무명: {}", name);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            JobResponseDTO job = jobMapper.findByName(name.trim());
            if (job == null) {
                log.debug("이름으로 직무 조회 결과 없음. name: {}", name);
                throw new BusinessException(ResponseCode.JOB_NOT_FOUND);
            }

            log.debug("이름으로 직무 조회 완료. name: {}, id: {}", name, job.getId());
            return CustomApiResponse.of(ResponseCode.SUCCESS, job);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("이름으로 직무 조회 중 오류 발생. name: {}", name, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithRoleInfo() {
        log.debug("권한 정보 포함 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findWithRoleInfo();
            log.debug("권한 정보 포함 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("권한 정보 포함 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithMemberCount() {
        log.debug("사원 수 정보 포함 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findWithMemberCount();
            log.debug("사원 수 정보 포함 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("사원 수 정보 포함 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithFullDetails() {
        log.debug("상세 정보 포함 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findWithFullDetails();
            log.debug("상세 정보 포함 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("상세 정보 포함 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsByRoleId(Integer roleId) {
        log.debug("권한 ID별 직무 조회 시작. roleId: {}", roleId);

        if (roleId == null || roleId <= 0) {
            log.warn("잘못된 권한 ID: {}", roleId);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> jobs = jobMapper.findByRoleId(roleId);
            log.debug("권한 ID별 직무 조회 완료. roleId: {}, 조회된 직무 수: {}", roleId, jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("권한 ID별 직무 조회 중 오류 발생. roleId: {}", roleId, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> searchJobsByName(String keyword) {
        log.debug("이름으로 직무 검색 시작. keyword: {}", keyword);

        if (!StringUtils.hasText(keyword)) {
            log.warn("검색 키워드가 비어있음: {}", keyword);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> jobs = jobMapper.findByNameContaining(keyword.trim());
            log.debug("이름으로 직무 검색 완료. keyword: {}, 조회된 직무 수: {}", keyword, jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("이름으로 직무 검색 중 오류 발생. keyword: {}", keyword, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> searchJobsByCode(String keyword) {
        log.debug("코드로 직무 검색 시작. keyword: {}", keyword);

        if (!StringUtils.hasText(keyword)) {
            log.warn("검색 키워드가 비어있음: {}", keyword);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> jobs = jobMapper.findByCodeContaining(keyword.trim());
            log.debug("코드로 직무 검색 완료. keyword: {}, 조회된 직무 수: {}", keyword, jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("코드로 직무 검색 중 오류 발생. keyword: {}", keyword, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> searchJobs(String keyword) {
        log.debug("통합 직무 검색 시작. keyword: {}", keyword);

        if (!StringUtils.hasText(keyword)) {
            log.warn("검색 키워드가 비어있음: {}", keyword);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> nameResults = jobMapper.findByNameContaining(keyword.trim());
            List<JobResponseDTO> codeResults = jobMapper.findByCodeContaining(keyword.trim());

            // 중복 제거 후 통합
            Set<Integer> uniqueIds = new HashSet<>();
            List<JobResponseDTO> combinedResults = new ArrayList<>();

            nameResults.forEach(job -> {
                if (uniqueIds.add(job.getId())) {
                    combinedResults.add(job);
                }
            });

            codeResults.forEach(job -> {
                if (uniqueIds.add(job.getId())) {
                    combinedResults.add(job);
                }
            });

            log.debug("통합 직무 검색 완료. keyword: {}, 조회된 직무 수: {}", keyword, combinedResults.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, combinedResults);
        } catch (Exception e) {
            log.error("통합 직무 검색 중 오류 발생. keyword: {}", keyword, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithDescription() {
        log.debug("설명이 있는 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findJobsWithDescription();
            log.debug("설명이 있는 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("설명이 있는 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithoutDescription() {
        log.debug("설명이 없는 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findJobsWithoutDescription();
            log.debug("설명이 없는 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("설명이 없는 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithMembers() {
        log.debug("사원이 있는 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findJobsWithMembers();
            log.debug("사원이 있는 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("사원이 있는 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithoutMembers() {
        log.debug("사원이 없는 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findJobsWithoutMembers();
            log.debug("사원이 없는 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("사원이 없는 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithRole() {
        log.debug("권한이 설정된 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findJobsWithRole();
            log.debug("권한이 설정된 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("권한이 설정된 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getJobsWithoutRole() {
        log.debug("권한이 설정되지 않은 직무 조회 시작");
        try {
            List<JobResponseDTO> jobs = jobMapper.findJobsWithoutRole();
            log.debug("권한이 설정되지 않은 직무 조회 완료. 조회된 직무 수: {}", jobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, jobs);
        } catch (Exception e) {
            log.error("권한이 설정되지 않은 직무 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> isCodeDuplicated(String code) {
        log.debug("직무 코드 중복 검사 시작. code: {}", code);

        if (!StringUtils.hasText(code)) {
            log.warn("잘못된 직무 코드: {}", code);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            boolean isDuplicated = jobMapper.existsByCode(code.trim());
            log.debug("직무 코드 중복 검사 완료. code: {}, isDuplicated: {}", code, isDuplicated);
            return CustomApiResponse.of(ResponseCode.SUCCESS, isDuplicated);
        } catch (Exception e) {
            log.error("직무 코드 중복 검사 중 오류 발생. code: {}", code, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> isNameDuplicated(String name) {
        log.debug("직무명 중복 검사 시작. name: {}", name);

        if (!StringUtils.hasText(name)) {
            log.warn("잘못된 직무명: {}", name);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            boolean isDuplicated = jobMapper.existsByName(name.trim());
            log.debug("직무명 중복 검사 완료. name: {}, isDuplicated: {}", name, isDuplicated);
            return CustomApiResponse.of(ResponseCode.SUCCESS, isDuplicated);
        } catch (Exception e) {
            log.error("직무명 중복 검사 중 오류 발생. name: {}", name, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> isCodeDuplicatedExcludingId(String code, Integer excludeId) {
        log.debug("직무 코드 중복 검사 (특정 ID 제외) 시작. code: {}, excludeId: {}", code, excludeId);

        if (!StringUtils.hasText(code)) {
            log.warn("잘못된 직무 코드: {}", code);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }
        if (excludeId == null || excludeId <= 0) {
            log.warn("잘못된 직무 ID: {}", excludeId);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            boolean isDuplicated = jobMapper.existsByCodeExcludingId(code.trim(), excludeId);
            log.debug("직무 코드 중복 검사 (특정 ID 제외) 완료. code: {}, excludeId: {}, isDuplicated: {}",
                    code, excludeId, isDuplicated);
            return CustomApiResponse.of(ResponseCode.SUCCESS, isDuplicated);
        } catch (Exception e) {
            log.error("직무 코드 중복 검사 (특정 ID 제외) 중 오류 발생. code: {}, excludeId: {}", code, excludeId, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> isNameDuplicatedExcludingId(String name, Integer excludeId) {
        log.debug("직무명 중복 검사 (특정 ID 제외) 시작. name: {}, excludeId: {}", name, excludeId);

        if (!StringUtils.hasText(name)) {
            log.warn("잘못된 직무명: {}", name);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }
        if (excludeId == null || excludeId <= 0) {
            log.warn("잘못된 직무 ID: {}", excludeId);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            boolean isDuplicated = jobMapper.existsByNameExcludingId(name.trim(), excludeId);
            log.debug("직무명 중복 검사 (특정 ID 제외) 완료. name: {}, excludeId: {}, isDuplicated: {}",
                    name, excludeId, isDuplicated);
            return CustomApiResponse.of(ResponseCode.SUCCESS, isDuplicated);
        } catch (Exception e) {
            log.error("직무명 중복 검사 (특정 ID 제외) 중 오류 발생. name: {}, excludeId: {}", name, excludeId, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> existsById(Integer id) {
        log.debug("직무 존재 여부 확인 (ID) 시작. id: {}", id);

        if (id == null || id <= 0) {
            return CustomApiResponse.of(ResponseCode.SUCCESS, false);
        }

        try {
            JobResponseDTO job = jobMapper.findById(id);
            boolean exists = job != null;
            log.debug("직무 존재 여부 확인 (ID) 완료. id: {}, exists: {}", id, exists);
            return CustomApiResponse.of(ResponseCode.SUCCESS, exists);
        } catch (Exception e) {
            log.error("직무 존재 여부 확인 (ID) 중 오류 발생. id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> existsByCode(String code) {
        log.debug("직무 존재 여부 확인 (코드) 시작. code: {}", code);

        if (!StringUtils.hasText(code)) {
            return CustomApiResponse.of(ResponseCode.SUCCESS, false);
        }

        try {
            boolean exists = jobMapper.existsByCode(code.trim());
            log.debug("직무 존재 여부 확인 (코드) 완료. code: {}, exists: {}", code, exists);
            return CustomApiResponse.of(ResponseCode.SUCCESS, exists);
        } catch (Exception e) {
            log.error("직무 존재 여부 확인 (코드) 중 오류 발생. code: {}", code, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Boolean> existsByName(String name) {
        log.debug("직무 존재 여부 확인 (이름) 시작. name: {}", name);

        if (!StringUtils.hasText(name)) {
            return CustomApiResponse.of(ResponseCode.SUCCESS, false);
        }

        try {
            boolean exists = jobMapper.existsByName(name.trim());
            log.debug("직무 존재 여부 확인 (이름) 완료. name: {}, exists: {}", name, exists);
            return CustomApiResponse.of(ResponseCode.SUCCESS, exists);
        } catch (Exception e) {
            log.error("직무 존재 여부 확인 (이름) 중 오류 발생. name: {}", name, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Map<String, Object>> getJobStatistics() {
        log.debug("직무 통계 정보 조회 시작");

        try {
            List<JobResponseDTO> allJobs = jobMapper.selectAllJobs();
            List<JobResponseDTO> activeJobs = jobMapper.findActiveJobs();
            List<JobResponseDTO> inactiveJobs = jobMapper.findInactiveJobs();
            List<JobResponseDTO> jobsWithMembers = jobMapper.findJobsWithMembers();
            List<JobResponseDTO> jobsWithRole = jobMapper.findJobsWithRole();

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalJobs", allJobs.size());
            statistics.put("activeJobs", activeJobs.size());
            statistics.put("inactiveJobs", inactiveJobs.size());
            statistics.put("jobsWithMembers", jobsWithMembers.size());
            statistics.put("jobsWithRole", jobsWithRole.size());
            statistics.put("jobsWithoutMembers", allJobs.size() - jobsWithMembers.size());
            statistics.put("jobsWithoutRole", allJobs.size() - jobsWithRole.size());

            log.debug("직무 통계 정보 조회 완료. 전체: {}, 활성: {}, 비활성: {}",
                    allJobs.size(), activeJobs.size(), inactiveJobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, statistics);
        } catch (Exception e) {
            log.error("직무 통계 정보 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Page<JobResponseDTO>> getJobsPaged(int page, int size, IsActive isActive) {
        log.debug("페이징 직무 조회 시작. page: {}, size: {}, isActive: {}", page, size, isActive);

        if (page < 0 || size <= 0) {
            log.warn("잘못된 페이징 파라미터. page: {}, size: {}", page, size);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> allJobs;
            if (isActive != null) {
                allJobs = jobMapper.findByIsActive(isActive.getValue());
            } else {
                allJobs = jobMapper.selectAllJobs();
            }

            Pageable pageable = PageRequest.of(page, size);
            int start = Math.min((int) pageable.getOffset(), allJobs.size());
            int end = Math.min((start + pageable.getPageSize()), allJobs.size());

            List<JobResponseDTO> pagedJobs = allJobs.subList(start, end);
            Page<JobResponseDTO> pagedResult = new PageImpl<>(pagedJobs, pageable, allJobs.size());

            log.debug("페이징 직무 조회 완료. 전체: {}, 페이지 크기: {}, 현재 페이지 항목 수: {}",
                    allJobs.size(), size, pagedJobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, pagedResult);
        } catch (Exception e) {
            log.error("페이징 직무 조회 중 오류 발생. page: {}, size: {}, isActive: {}", page, size, isActive, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Map<String, Object>> validateJob(Integer id) {
        log.debug("직무 유효성 검증 시작. id: {}", id);

        try {
            Map<String, Object> validation = new HashMap<>();

            if (id == null || id <= 0) {
                validation.put("valid", false);
                validation.put("message", "유효하지 않은 직무 ID입니다.");
                return CustomApiResponse.of(ResponseCode.SUCCESS, validation);
            }

            JobResponseDTO job = jobMapper.findById(id);
            if (job == null) {
                validation.put("valid", false);
                validation.put("message", "해당 ID의 직무를 찾을 수 없습니다.");
            } else {
                validation.put("valid", true);
                validation.put("message", "유효한 직무입니다.");
                validation.put("job", job);
            }

            log.debug("직무 유효성 검증 완료. id: {}, valid: {}", id, validation.get("valid"));
            return CustomApiResponse.of(ResponseCode.SUCCESS, validation);
        } catch (Exception e) {
            log.error("직무 유효성 검증 중 오류 발생. id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<Map<String, Integer>> getJobMemberCountSummary() {
        log.debug("직무별 사원 수 집계 조회 시작");

        try {
            List<JobResponseDTO> jobsWithCount = jobMapper.findWithMemberCount();
            Map<String, Integer> summary = jobsWithCount.stream()
                    .collect(Collectors.toMap(
                            JobResponseDTO::getName,
                            job -> job.getMemberCount() != null ? job.getMemberCount() : 0
                    ));

            log.debug("직무별 사원 수 집계 조회 완료. 직무 수: {}", summary.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, summary);
        } catch (Exception e) {
            log.error("직무별 사원 수 집계 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getTopJobsByMemberCount(int limit) {
        log.debug("사원 수 기준 상위 직무 조회 시작. limit: {}", limit);

        if (limit <= 0) {
            log.warn("잘못된 조회 개수: {}", limit);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> jobsWithCount = jobMapper.findWithMemberCount();
            List<JobResponseDTO> topJobs = jobsWithCount.stream()
                    .sorted((a, b) -> Integer.compare(
                            b.getMemberCount() != null ? b.getMemberCount() : 0,
                            a.getMemberCount() != null ? a.getMemberCount() : 0
                    ))
                    .limit(limit)
                    .collect(Collectors.toList());

            log.debug("사원 수 기준 상위 직무 조회 완료. limit: {}, 조회된 수: {}", limit, topJobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, topJobs);
        } catch (Exception e) {
            log.error("사원 수 기준 상위 직무 조회 중 오류 발생. limit: {}", limit, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getRecentlyCreatedJobs(int limit) {
        log.debug("최근 생성된 직무 조회 시작. limit: {}", limit);

        if (limit <= 0) {
            log.warn("잘못된 조회 개수: {}", limit);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> allJobs = jobMapper.selectAllJobs();
            List<JobResponseDTO> recentJobs = allJobs.stream()
                    .sorted((a, b) -> {
                        if (a.getCreatedAt() != null && b.getCreatedAt() != null) {
                            return b.getCreatedAt().compareTo(a.getCreatedAt());
                        }
                        return 0;
                    })
                    .limit(limit)
                    .collect(Collectors.toList());

            log.debug("최근 생성된 직무 조회 완료. limit: {}, 조회된 수: {}", limit, recentJobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, recentJobs);
        } catch (Exception e) {
            log.error("최근 생성된 직무 조회 중 오류 발생. limit: {}", limit, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<JobResponseDTO>> getRecentlyUpdatedJobs(int limit) {
        log.debug("최근 수정된 직무 조회 시작. limit: {}", limit);

        if (limit <= 0) {
            log.warn("잘못된 조회 개수: {}", limit);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<JobResponseDTO> allJobs = jobMapper.selectAllJobs();
            List<JobResponseDTO> recentJobs = allJobs.stream()
                    .sorted((a, b) -> {
                        if (a.getUpdatedAt() != null && b.getUpdatedAt() != null) {
                            return b.getUpdatedAt().compareTo(a.getUpdatedAt());
                        }
                        return 0;
                    })
                    .limit(limit)
                    .collect(Collectors.toList());

            log.debug("최근 수정된 직무 조회 완료. limit: {}, 조회된 수: {}", limit, recentJobs.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, recentJobs);
        } catch (Exception e) {
            log.error("최근 수정된 직무 조회 중 오류 발생. limit: {}", limit, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}