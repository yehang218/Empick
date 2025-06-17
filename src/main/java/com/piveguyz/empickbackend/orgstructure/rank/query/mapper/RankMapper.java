package com.piveguyz.empickbackend.orgstructure.rank.query.mapper;

import com.piveguyz.empickbackend.orgstructure.rank.query.dto.RankQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 직급 조회용 MyBatis Mapper 인터페이스
 * 복잡한 조인 쿼리와 집계 함수를 활용한 직급 조회 기능 제공
 */
@Mapper
public interface RankMapper {

    /**
     * 모든 직급 조회 (활성/비활성 구분 없이)
     * 기본적인 직급 정보만 조회
     *
     * @return 전체 직급 목록
     */
    List<RankQueryDTO> findAll();

    /**
     * 활성 여부별 직급 조회
     *
     * @param isActive 활성 여부 (1=활성, 0=비활성)
     * @return 조건에 맞는 직급 목록
     */
    List<RankQueryDTO> findByIsActive(@Param("isActive") Integer isActive);

    /**
     * ID로 직급 단건 조회
     *
     * @param id 직급 ID
     * @return 직급 정보 (없으면 null)
     */
    RankQueryDTO findById(@Param("id") Integer id);

    /**
     * 코드로 직급 단건 조회
     *
     * @param code 직급 코드
     * @return 직급 정보 (없으면 null)
     */
    RankQueryDTO findByCode(@Param("code") String code);

    /**
     * 권한 정보를 포함한 직급 조회
     * rank 테이블과 role 테이블을 조인하여 권한 정보 포함
     *
     * @return 권한 정보가 포함된 직급 목록
     */
    List<RankQueryDTO> findWithRoleInfo();

    /**
     * 사원 수 정보를 포함한 직급 조회
     * rank 테이블과 member 테이블을 조인하여 사원 수 집계
     *
     * @return 사원 수 정보가 포함된 직급 목록
     */
    List<RankQueryDTO> findWithMemberCount();

    /**
     * 권한 정보와 사원 수 정보를 모두 포함한 상세 직급 조회
     * rank, role, member 테이블을 모두 조인한 완전한 정보
     *
     * @return 상세 정보가 포함된 직급 목록
     */
    List<RankQueryDTO> findWithFullDetails();

    /**
     * 특정 권한을 가진 직급 조회
     *
     * @param roleId 권한 ID
     * @return 해당 권한을 가진 직급 목록
     */
    List<RankQueryDTO> findByRoleId(@Param("roleId") Integer roleId);

    /**
     * 급여 밴드 범위로 직급 조회
     *
     * @param minSalaryBand 최소 급여 밴드 (null 허용)
     * @param maxSalaryBand 최대 급여 밴드 (null 허용)
     * @return 급여 밴드 범위에 해당하는 직급 목록
     */
    List<RankQueryDTO> findBySalaryBandRange(
            @Param("minSalaryBand") Integer minSalaryBand,
            @Param("maxSalaryBand") Integer maxSalaryBand
    );

    /**
     * 사원이 있는 직급만 조회
     * member_count > 0인 직급만 반환
     *
     * @return 사원이 배정된 직급 목록
     */
    List<RankQueryDTO> findRanksWithMembers();

    /**
     * 사원이 없는 직급만 조회  
     * member_count = 0 또는 null인 직급만 반환
     *
     * @return 사원이 배정되지 않은 직급 목록
     */
    List<RankQueryDTO> findRanksWithoutMembers();
}