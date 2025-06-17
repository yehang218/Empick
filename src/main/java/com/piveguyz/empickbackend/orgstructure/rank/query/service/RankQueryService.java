package com.piveguyz.empickbackend.orgstructure.rank.query.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.rank.query.dto.RankQueryDTO;

import java.util.List;

/**
 * 직급 조회 서비스 인터페이스
 * MyBatis 기반의 복잡한 조회 쿼리를 처리하고 CustomApiResponse로 반환
 */
public interface RankQueryService {

    /**
     * 모든 직급 조회 (활성/비활성 구분 없이)
     * @return 전체 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findAll();

    /**
     * 활성 여부별 직급 조회
     * @param isActive 활성 여부 (ACTIVE=1, INACTIVE=0)
     * @return 조건에 맞는 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findByIsActive(IsActive isActive);

    /**
     * 활성 직급만 조회 (편의 메서드)
     * @return 활성 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findActiveRanks();

    /**
     * 비활성 직급만 조회 (편의 메서드)
     * @return 비활성 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findInactiveRanks();

    /**
     * ID로 직급 단건 조회
     * @param id 직급 ID
     * @return 직급 정보를 포함한 API 응답
     */
    CustomApiResponse<RankQueryDTO> findById(Integer id);

    /**
     * 코드로 직급 단건 조회
     * @param code 직급 코드
     * @return 직급 정보를 포함한 API 응답
     */
    CustomApiResponse<RankQueryDTO> findByCode(String code);

    /**
     * 권한 정보를 포함한 직급 조회
     * @return 권한 정보가 포함된 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findWithRoleInfo();

    /**
     * 사원 수 정보를 포함한 직급 조회
     * @return 사원 수 정보가 포함된 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findWithMemberCount();

    /**
     * 권한 정보와 사원 수 정보를 모두 포함한 상세 직급 조회
     * @return 상세 정보가 포함된 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findWithFullDetails();

    /**
     * 특정 권한을 가진 직급 조회
     * @param roleId 권한 ID
     * @return 해당 권한을 가진 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findByRoleId(Integer roleId);

    /**
     * 급여 밴드 범위로 직급 조회
     * @param minSalaryBand 최소 급여 밴드
     * @param maxSalaryBand 최대 급여 밴드
     * @return 급여 밴드 범위에 해당하는 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findBySalaryBandRange(Integer minSalaryBand, Integer maxSalaryBand);

    /**
     * 사원이 있는 직급만 조회
     * @return 사원이 배정된 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findRanksWithMembers();

    /**
     * 사원이 없는 직급만 조회
     * @return 사원이 배정되지 않은 직급 목록을 포함한 API 응답
     */
    CustomApiResponse<List<RankQueryDTO>> findRanksWithoutMembers();
}