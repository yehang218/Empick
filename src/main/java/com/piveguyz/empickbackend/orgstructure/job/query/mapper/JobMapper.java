package com.piveguyz.empickbackend.orgstructure.job.query.mapper;

import com.piveguyz.empickbackend.orgstructure.job.query.dto.JobResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 직무 조회용 MyBatis Mapper 인터페이스
 * 복잡한 조인 쿼리와 집계 함수를 활용한 직무 조회 기능 제공
 */
@Mapper
public interface JobMapper {

    /**
     * 모든 직무 조회 (활성/비활성 구분 없이)
     * 기본적인 직무 정보만 조회
     *
     * @return 전체 직무 목록
     */
    List<JobResponseDTO> selectAllJobs();

    /**
     * 활성 여부별 직무 조회
     *
     * @param isActive 활성 여부 (1=활성, 0=비활성)
     * @return 조건에 맞는 직무 목록
     */
    List<JobResponseDTO> findByIsActive(@Param("isActive") Integer isActive);

    /**
     * ID로 직무 단건 조회
     *
     * @param id 직무 ID
     * @return 직무 정보 (없으면 null)
     */
    JobResponseDTO findById(@Param("id") Integer id);

    /**
     * 코드로 직무 단건 조회
     *
     * @param code 직무 코드
     * @return 직무 정보 (없으면 null)
     */
    JobResponseDTO findByCode(@Param("code") String code);

    /**
     * 이름으로 직무 단건 조회
     *
     * @param name 직무명
     * @return 직무 정보 (없으면 null)
     */
    JobResponseDTO findByName(@Param("name") String name);

    /**
     * 코드로 직무 존재 여부 확인
     *
     * @param code 직무 코드
     * @return 존재 여부
     */
    boolean existsByCode(@Param("code") String code);

    /**
     * 이름으로 직무 존재 여부 확인
     *
     * @param name 직무명
     * @return 존재 여부
     */
    boolean existsByName(@Param("name") String name);

    /**
     * 권한 정보를 포함한 직무 조회
     * job 테이블과 role 테이블을 조인하여 권한 정보 포함
     *
     * @return 권한 정보가 포함된 직무 목록
     */
    List<JobResponseDTO> findWithRoleInfo();

    /**
     * 사원 수 정보를 포함한 직무 조회
     * job 테이블과 member 테이블을 조인하여 사원 수 집계
     *
     * @return 사원 수 정보가 포함된 직무 목록
     */
    List<JobResponseDTO> findWithMemberCount();

    /**
     * 권한 정보와 사원 수 정보를 모두 포함한 상세 직무 조회
     * job, role, member 테이블을 모두 조인한 완전한 정보
     *
     * @return 상세 정보가 포함된 직무 목록
     */
    List<JobResponseDTO> findWithFullDetails();

    /**
     * 특정 권한을 가진 직무 조회
     *
     * @param roleId 권한 ID
     * @return 해당 권한을 가진 직무 목록
     */
    List<JobResponseDTO> findByRoleId(@Param("roleId") Integer roleId);

    /**
     * 이름으로 직무 검색 (부분 일치)
     *
     * @param keyword 검색 키워드
     * @return 이름에 키워드가 포함된 직무 목록
     */
    List<JobResponseDTO> findByNameContaining(@Param("keyword") String keyword);

    /**
     * 코드로 직무 검색 (부분 일치)
     *
     * @param keyword 검색 키워드
     * @return 코드에 키워드가 포함된 직무 목록
     */
    List<JobResponseDTO> findByCodeContaining(@Param("keyword") String keyword);

    /**
     * 설명이 있는 직무만 조회
     *
     * @return 설명이 등록된 직무 목록
     */
    List<JobResponseDTO> findJobsWithDescription();

    /**
     * 설명이 없는 직무만 조회
     *
     * @return 설명이 등록되지 않은 직무 목록
     */
    List<JobResponseDTO> findJobsWithoutDescription();

    /**
     * 사원이 있는 직무만 조회
     * member_count > 0인 직무만 반환
     *
     * @return 사원이 배정된 직무 목록
     */
    List<JobResponseDTO> findJobsWithMembers();

    /**
     * 사원이 없는 직무만 조회
     * member_count = 0 또는 null인 직무만 반환
     *
     * @return 사원이 배정되지 않은 직무 목록
     */
    List<JobResponseDTO> findJobsWithoutMembers();

    /**
     * 특정 ID를 제외한 이름 중복 검사 (수정 시 사용)
     *
     * @param name 직무명
     * @param excludeId 제외할 직무 ID
     * @return 중복 존재 여부
     */
    boolean existsByNameExcludingId(@Param("name") String name, @Param("excludeId") Integer excludeId);

    /**
     * 특정 ID를 제외한 코드 중복 검사 (수정 시 사용)
     *
     * @param code 직무 코드
     * @param excludeId 제외할 직무 ID
     * @return 중복 존재 여부
     */
    boolean existsByCodeExcludingId(@Param("code") String code, @Param("excludeId") Integer excludeId);

    /**
     * 권한이 연결된 직무 조회
     *
     * @return 권한이 설정된 직무 목록
     */
    List<JobResponseDTO> findJobsWithRole();

    /**
     * 권한이 연결되지 않은 직무 조회
     *
     * @return 권한이 설정되지 않은 직무 목록
     */
    List<JobResponseDTO> findJobsWithoutRole();

    /**
     * 활성 직무만 조회
     *
     * @return 활성 상태인 직무 목록
     */
    List<JobResponseDTO> findActiveJobs();

    /**
     * 비활성 직무만 조회
     *
     * @return 비활성 상태인 직무 목록
     */
    List<JobResponseDTO> findInactiveJobs();
}