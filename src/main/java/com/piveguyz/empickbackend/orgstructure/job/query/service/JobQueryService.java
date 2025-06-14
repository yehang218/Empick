package com.piveguyz.empickbackend.orgstructure.job.query.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 직무 조회 서비스 인터페이스
 * 다양한 조건과 필터를 활용한 직무 조회 기능을 제공하며 CustomApiResponse로 응답
 */
public interface JobQueryService {

    /**
     * 모든 직무 조회 (기본 메서드)
     * 활성/비활성 구분 없이 모든 직무 정보 조회
     *
     * @return 전체 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getAllJobs();

    /**
     * 활성 여부별 직무 조회
     *
     * @param isActive 활성 여부 (ACTIVE=1, INACTIVE=0)
     * @return 조건에 맞는 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsByActiveStatus(IsActive isActive);

    /**
     * 활성 직무만 조회 (편의 메서드)
     *
     * @return 활성 상태인 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getActiveJobs();

    /**
     * 비활성 직무만 조회 (편의 메서드)
     *
     * @return 비활성 상태인 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getInactiveJobs();

    /**
     * ID로 직무 단건 조회
     *
     * @param id 직무 ID
     * @return 직무 정보를 포함한 API 응답
     */
    CustomApiResponse<JobResponseDTO> getJobById(Integer id);

    /**
     * 코드로 직무 단건 조회
     *
     * @param code 직무 코드
     * @return 직무 정보를 포함한 API 응답
     */
    CustomApiResponse<JobResponseDTO> getJobByCode(String code);

    /**
     * 이름으로 직무 단건 조회
     *
     * @param name 직무명
     * @return 직무 정보를 포함한 API 응답
     */
    CustomApiResponse<JobResponseDTO> getJobByName(String name);

    /**
     * 권한 정보를 포함한 직무 조회
     * job 테이블과 role 테이블을 조인하여 권한 정보 포함
     *
     * @return 권한 정보가 포함된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithRoleInfo();

    /**
     * 사원 수 정보를 포함한 직무 조회
     * job 테이블과 member 테이블을 조인하여 사원 수 집계
     *
     * @return 사원 수 정보가 포함된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithMemberCount();

    /**
     * 권한 정보와 사원 수 정보를 모두 포함한 상세 직무 조회
     * job, role, member 테이블을 모두 조인한 완전한 정보
     *
     * @return 상세 정보가 포함된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithFullDetails();

    /**
     * 특정 권한을 가진 직무 조회
     *
     * @param roleId 권한 ID
     * @return 해당 권한을 가진 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsByRoleId(Integer roleId);

    /**
     * 직무명으로 검색 (부분 일치)
     *
     * @param keyword 검색 키워드
     * @return 이름에 키워드가 포함된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> searchJobsByName(String keyword);

    /**
     * 직무 코드로 검색 (부분 일치)
     *
     * @param keyword 검색 키워드
     * @return 코드에 키워드가 포함된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> searchJobsByCode(String keyword);

    /**
     * 통합 검색 (이름 또는 코드에서 검색)
     *
     * @param keyword 검색 키워드
     * @return 이름 또는 코드에 키워드가 포함된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> searchJobs(String keyword);

    /**
     * 설명이 있는 직무만 조회
     *
     * @return 설명이 등록된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithDescription();

    /**
     * 설명이 없는 직무만 조회
     *
     * @return 설명이 등록되지 않은 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithoutDescription();

    /**
     * 사원이 배정된 직무만 조회
     *
     * @return 사원이 배정된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithMembers();

    /**
     * 사원이 배정되지 않은 직무만 조회
     *
     * @return 사원이 배정되지 않은 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithoutMembers();

    /**
     * 권한이 설정된 직무 조회
     *
     * @return 권한이 설정된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithRole();

    /**
     * 권한이 설정되지 않은 직무 조회
     *
     * @return 권한이 설정되지 않은 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getJobsWithoutRole();

    /**
     * 직무 코드 중복 검사
     *
     * @param code 직무 코드
     * @return 중복 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> isCodeDuplicated(String code);

    /**
     * 직무명 중복 검사
     *
     * @param name 직무명
     * @return 중복 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> isNameDuplicated(String name);

    /**
     * 특정 ID를 제외한 코드 중복 검사 (수정 시 사용)
     *
     * @param code 직무 코드
     * @param excludeId 제외할 직무 ID
     * @return 중복 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> isCodeDuplicatedExcludingId(String code, Integer excludeId);

    /**
     * 특정 ID를 제외한 이름 중복 검사 (수정 시 사용)
     *
     * @param name 직무명
     * @param excludeId 제외할 직무 ID
     * @return 중복 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> isNameDuplicatedExcludingId(String name, Integer excludeId);

    /**
     * 직무 존재 여부 확인 (ID 기준)
     *
     * @param id 직무 ID
     * @return 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> existsById(Integer id);

    /**
     * 직무 존재 여부 확인 (코드 기준)
     *
     * @param code 직무 코드
     * @return 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> existsByCode(String code);

    /**
     * 직무 존재 여부 확인 (이름 기준)
     *
     * @param name 직무명
     * @return 존재 여부를 포함한 API 응답
     */
    CustomApiResponse<Boolean> existsByName(String name);

    /**
     * 직무 통계 정보 조회
     * 전체, 활성, 비활성 직무 수 및 사원이 배정된 직무 수 등
     *
     * @return 직무 통계 정보를 담은 Map을 포함한 API 응답
     */
    CustomApiResponse<Map<String, Object>> getJobStatistics();

    /**
     * 페이징을 지원하는 직무 조회
     *
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 크기
     * @param isActive 활성 여부 필터 (null이면 전체 조회)
     * @return 페이징된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<Page<JobResponseDTO>> getJobsPaged(int page, int size, IsActive isActive);

    /**
     * 직무 유효성 검증
     *
     * @param id 직무 ID
     * @return 유효성 검증 결과를 포함한 API 응답
     */
    CustomApiResponse<Map<String, Object>> validateJob(Integer id);

    /**
     * 직무별 사원 수 집계 조회
     *
     * @return 직무별 사원 수 집계 정보를 포함한 API 응답
     */
    CustomApiResponse<Map<String, Integer>> getJobMemberCountSummary();

    /**
     * 가장 많이 사용되는 직무 TOP N 조회
     *
     * @param limit 조회할 상위 개수
     * @return 사원 수 기준 상위 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getTopJobsByMemberCount(int limit);

    /**
     * 최근 생성된 직무 조회
     *
     * @param limit 조회할 개수
     * @return 최근 생성된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getRecentlyCreatedJobs(int limit);

    /**
     * 최근 수정된 직무 조회
     *
     * @param limit 조회할 개수
     * @return 최근 수정된 직무 목록을 포함한 API 응답
     */
    CustomApiResponse<List<JobResponseDTO>> getRecentlyUpdatedJobs(int limit);
}