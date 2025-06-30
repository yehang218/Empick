package com.piveguyz.empickbackend.orgstructure.position.query.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.orgstructure.position.query.dto.PositionQueryDTO;

import java.util.List;

/**
 * 직책 조회 서비스 인터페이스
 * MyBatis 기반의 복잡한 조회 쿼리를 처리하고 CustomApiResponse로 반환
 */
public interface PositionQueryService {

    /**
     * 모든 직책 조회 (활성/비활성 구분 없이)
     * @return 전체 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findAll();

    /**
     * 활성 여부별 직책 조회
     * @param isActive 활성 여부 (ACTIVE=1, INACTIVE=0)
     * @return 조건에 맞는 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findByIsActive(IsActive isActive);

    /**
     * 활성 직책만 조회 (편의 메서드)
     * @return 활성 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findActivePositions();

    /**
     * 비활성 직책만 조회 (편의 메서드)
     * @return 비활성 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findInactivePositions();

    /**
     * ID로 직책 단건 조회
     * @param id 직책 ID
     * @return 직책 정보를 포함한 API 응답
     */
    CustomApiResponse<PositionQueryDTO> findById(Integer id);

    /**
     * 코드로 직책 단건 조회
     * @param code 직책 코드
     * @return 직책 정보를 포함한 API 응답
     */
    CustomApiResponse<PositionQueryDTO> findByCode(String code);

    /**
     * 이름으로 직책 단건 조회
     * @param name 직책명
     * @return 직책 정보를 포함한 API 응답
     */
    CustomApiResponse<PositionQueryDTO> findByName(String name);

    /**
     * 권한 정보를 포함한 직책 조회
     * @return 권한 정보가 포함된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findWithRoleInfo();

    /**
     * 사원 수 정보를 포함한 직책 조회
     * @return 사원 수 정보가 포함된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findWithMemberCount();

    /**
     * 권한 정보와 사원 수 정보를 모두 포함한 상세 직책 조회
     * @return 상세 정보가 포함된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findWithFullDetails();

    /**
     * 특정 권한을 가진 직책 조회
     * @param roleId 권한 ID
     * @return 해당 권한을 가진 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findByRoleId(Integer roleId);

    /**
     * 이름으로 직책 검색 (부분 일치)
     * @param keyword 검색 키워드
     * @return 키워드가 포함된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findByNameContaining(String keyword);

    /**
     * 코드로 직책 검색 (부분 일치)
     * @param keyword 검색 키워드
     * @return 코드에 키워드가 포함된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findByCodeContaining(String keyword);

    /**
     * 설명이 있는 직책만 조회
     * @return 설명이 등록된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findPositionsWithDescription();

    /**
     * 설명이 없는 직책만 조회
     * @return 설명이 등록되지 않은 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findPositionsWithoutDescription();

    /**
     * 사원이 있는 직책만 조회
     * @return 사원이 배정된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findPositionsWithMembers();

    /**
     * 사원이 없는 직책만 조회
     * @return 사원이 배정되지 않은 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findPositionsWithoutMembers();

    /**
     * 권한이 연결된 직책 조회
     * @return 권한이 설정된 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findPositionsWithRole();

    /**
     * 권한이 연결되지 않은 직책 조회
     * @return 권한이 설정되지 않은 직책 목록을 포함한 API 응답
     */
    CustomApiResponse<List<PositionQueryDTO>> findPositionsWithoutRole();
}