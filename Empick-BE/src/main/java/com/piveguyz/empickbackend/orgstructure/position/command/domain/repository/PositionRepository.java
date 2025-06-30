
package com.piveguyz.empickbackend.orgstructure.position.command.domain.repository;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.position.command.domain.aggregate.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 직책 레포지토리
 * 직책 엔티티에 대한 데이터 액세스 기능 제공
 */
@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {

    /**
     * 직책명으로 조회
     *
     * @param name 직책명
     * @return 직책 엔티티
     */
    Optional<PositionEntity> findByName(String name);

    /**
     * 직책 코드로 조회
     *
     * @param code 직책 코드
     * @return 직책 엔티티
     */
    Optional<PositionEntity> findByCode(String code);

    /**
     * 직책명으로 조회 (대소문자 구분 없음)
     *
     * @param name 직책명
     * @return 직책 엔티티
     */
    Optional<PositionEntity> findByNameIgnoreCase(String name);

    /**
     * 직책 코드로 조회 (대소문자 구분 없음)
     *
     * @param code 직책 코드
     * @return 직책 엔티티
     */
    Optional<PositionEntity> findByCodeIgnoreCase(String code);

    /**
     * 활성 여부로 조회
     *
     * @param isActive 활성 여부
     * @return 직책 목록
     */
    List<PositionEntity> findByIsActive(IsActive isActive);

    /**
     * 권한 ID로 조회
     *
     * @param roleId 권한 ID
     * @return 직책 목록
     */
    List<PositionEntity> findByRoleId(Integer roleId);

    /**
     * 권한이 연결된 직책 조회
     *
     * @return 권한이 있는 직책 목록
     */
    List<PositionEntity> findByRoleIdIsNotNull();

    /**
     * 권한이 연결되지 않은 직책 조회
     *
     * @return 권한이 없는 직책 목록
     */
    List<PositionEntity> findByRoleIdIsNull();

    /**
     * 직책명으로 검색 (부분 일치)
     *
     * @param keyword 검색 키워드
     * @return 직책 목록
     */
    List<PositionEntity> findByNameContainingIgnoreCase(String keyword);

    /**
     * 직책 코드로 검색 (부분 일치)
     *
     * @param keyword 검색 키워드
     * @return 직책 목록
     */
    List<PositionEntity> findByCodeContainingIgnoreCase(String keyword);

    /**
     * 활성 상태이면서 특정 권한을 가진 직책 조회
     *
     * @param isActive 활성 여부
     * @param roleId 권한 ID
     * @return 직책 목록
     */
    List<PositionEntity> findByIsActiveAndRoleId(IsActive isActive, Integer roleId);

    /**
     * 설명이 있는 직책 조회
     *
     * @return 설명이 있는 직책 목록
     */
    @Query("SELECT p FROM PositionEntity p WHERE p.description IS NOT NULL AND TRIM(p.description) != ''")
    List<PositionEntity> findPositionsWithDescription();

    /**
     * 설명이 없는 직책 조회
     *
     * @return 설명이 없는 직책 목록
     */
    @Query("SELECT p FROM PositionEntity p WHERE p.description IS NULL OR TRIM(p.description) = ''")
    List<PositionEntity> findPositionsWithoutDescription();

    /**
     * 직책명 중복 확인
     *
     * @param name 확인할 직책명
     * @return 중복 여부
     */
    boolean existsByName(String name);

    /**
     * 직책 코드 중복 확인
     *
     * @param code 확인할 직책 코드
     * @return 중복 여부
     */
    boolean existsByCode(String code);

    /**
     * 직책명 중복 확인 (자기 제외)
     *
     * @param name 확인할 직책명
     * @param id 제외할 직책 ID
     * @return 중복 여부
     */
    boolean existsByNameAndIdNot(String name, Integer id);

    /**
     * 직책 코드 중복 확인 (자기 제외)
     *
     * @param code 확인할 직책 코드
     * @param id 제외할 직책 ID
     * @return 중복 여부
     */
    boolean existsByCodeAndIdNot(String code, Integer id);

    /**
     * 특정 권한을 사용하는 직책 수 조회
     *
     * @param roleId 권한 ID
     * @return 직책 수
     */
    long countByRoleId(Integer roleId);

    /**
     * 활성 상태별 직책 수 조회
     *
     * @param isActive 활성 여부
     * @return 직책 수
     */
    long countByIsActive(IsActive isActive);

    /**
     * 특정 권한을 가진 직책들의 활성 상태 일괄 변경
     *
     * @param roleId 권한 ID
     * @param isActive 변경할 활성 상태
     * @return 업데이트된 행 수
     */
    @Modifying
    @Query("UPDATE PositionEntity p SET p.isActive = :isActive WHERE p.roleId = :roleId")
    int updateIsActiveByRoleId(@Param("roleId") Integer roleId, @Param("isActive") IsActive isActive);

    /**
     * 특정 권한 연결 해제
     *
     * @param roleId 해제할 권한 ID
     * @return 업데이트된 행 수
     */
    @Modifying
    @Query("UPDATE PositionEntity p SET p.roleId = null WHERE p.roleId = :roleId")
    int disconnectRole(@Param("roleId") Integer roleId);

    /**
     * 활성 상태별 직책을 이름 순으로 조회
     *
     * @param isActive 활성 여부
     * @return 정렬된 직책 목록
     */
    List<PositionEntity> findByIsActiveOrderByName(IsActive isActive);

    /**
     * 활성 상태별 직책을 코드 순으로 조회
     *
     * @param isActive 활성 여부
     * @return 정렬된 직책 목록
     */
    List<PositionEntity> findByIsActiveOrderByCode(IsActive isActive);

    /**
     * 모든 직책을 이름 순으로 조회
     *
     * @return 정렬된 직책 목록
     */
    List<PositionEntity> findAllByOrderByName();

    /**
     * 모든 직책을 코드 순으로 조회
     *
     * @return 정렬된 직책 목록
     */
    List<PositionEntity> findAllByOrderByCode();

    /**
     * 생성일 기준으로 최근 직책 조회
     *
     * @param limit 조회할 개수
     * @return 최근 생성된 직책 목록
     */
    @Query("SELECT p FROM PositionEntity p ORDER BY p.createdAt DESC LIMIT :limit")
    List<PositionEntity> findRecentPositions(@Param("limit") int limit);
}