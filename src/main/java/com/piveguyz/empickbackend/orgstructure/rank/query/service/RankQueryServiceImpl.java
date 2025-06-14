package com.piveguyz.empickbackend.orgstructure.rank.query.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.rank.query.dto.RankQueryDTO;
import com.piveguyz.empickbackend.orgstructure.rank.query.mapper.RankMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 직급 조회 서비스 구현체
 * MyBatis 기반 복잡한 조회 로직 처리 및 CustomApiResponse 반환
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankQueryServiceImpl implements RankQueryService {

    private final RankMapper rankMapper;

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findAll() {
        log.debug("모든 직급 조회 시작");
        try {
            List<RankQueryDTO> ranks = rankMapper.findAll();
            log.debug("모든 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("모든 직급 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findByIsActive(IsActive isActive) {
        log.debug("활성 여부별 직급 조회 시작. isActive: {}", isActive);
        try {
            List<RankQueryDTO> ranks = rankMapper.findByIsActive(isActive.getValue());
            log.debug("활성 여부별 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("활성 여부별 직급 조회 중 오류 발생. isActive: {}", isActive, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findActiveRanks() {
        log.debug("활성 직급 조회 시작");
        return findByIsActive(IsActive.ACTIVE);
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findInactiveRanks() {
        log.debug("비활성 직급 조회 시작");
        return findByIsActive(IsActive.INACTIVE);
    }

    @Override
    public CustomApiResponse<RankQueryDTO> findById(Integer id) {
        log.debug("ID로 직급 조회 시작. id: {}", id);

        if (id == null || id <= 0) {
            log.warn("잘못된 직급 ID: {}", id);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            RankQueryDTO rank = rankMapper.findById(id);
            if (rank == null) {
                log.debug("ID로 직급 조회 결과 없음. id: {}", id);
                throw new BusinessException(ResponseCode.RANK_NOT_FOUND);
            }

            log.debug("ID로 직급 조회 완료. id: {}", id);
            return CustomApiResponse.of(ResponseCode.SUCCESS, rank);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("ID로 직급 조회 중 오류 발생. id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<RankQueryDTO> findByCode(String code) {
        log.debug("코드로 직급 조회 시작. code: {}", code);

        if (code == null || code.trim().isEmpty()) {
            log.warn("잘못된 직급 코드: {}", code);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            RankQueryDTO rank = rankMapper.findByCode(code.trim());
            if (rank == null) {
                log.debug("코드로 직급 조회 결과 없음. code: {}", code);
                throw new BusinessException(ResponseCode.RANK_NOT_FOUND);
            }

            log.debug("코드로 직급 조회 완료. code: {}", code);
            return CustomApiResponse.of(ResponseCode.SUCCESS, rank);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("코드로 직급 조회 중 오류 발생. code: {}", code, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findWithRoleInfo() {
        log.debug("권한 정보 포함 직급 조회 시작");
        try {
            List<RankQueryDTO> ranks = rankMapper.findWithRoleInfo();
            log.debug("권한 정보 포함 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("권한 정보 포함 직급 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findWithMemberCount() {
        log.debug("사원 수 정보 포함 직급 조회 시작");
        try {
            List<RankQueryDTO> ranks = rankMapper.findWithMemberCount();
            log.debug("사원 수 정보 포함 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("사원 수 정보 포함 직급 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findWithFullDetails() {
        log.debug("상세 정보 포함 직급 조회 시작");
        try {
            List<RankQueryDTO> ranks = rankMapper.findWithFullDetails();
            log.debug("상세 정보 포함 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("상세 정보 포함 직급 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findByRoleId(Integer roleId) {
        log.debug("권한 ID별 직급 조회 시작. roleId: {}", roleId);

        if (roleId == null || roleId <= 0) {
            log.warn("잘못된 권한 ID: {}", roleId);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<RankQueryDTO> ranks = rankMapper.findByRoleId(roleId);
            log.debug("권한 ID별 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("권한 ID별 직급 조회 중 오류 발생. roleId: {}", roleId, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findBySalaryBandRange(Integer minSalaryBand, Integer maxSalaryBand) {
        log.debug("급여 밴드 범위별 직급 조회 시작. min: {}, max: {}", minSalaryBand, maxSalaryBand);

        // 파라미터 검증
        if (minSalaryBand != null && maxSalaryBand != null && minSalaryBand > maxSalaryBand) {
            log.warn("잘못된 급여 밴드 범위. min: {}, max: {}", minSalaryBand, maxSalaryBand);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<RankQueryDTO> ranks = rankMapper.findBySalaryBandRange(minSalaryBand, maxSalaryBand);
            log.debug("급여 밴드 범위별 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("급여 밴드 범위별 직급 조회 중 오류 발생. min: {}, max: {}", minSalaryBand, maxSalaryBand, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findRanksWithMembers() {
        log.debug("사원이 있는 직급 조회 시작");
        try {
            List<RankQueryDTO> ranks = rankMapper.findRanksWithMembers();
            log.debug("사원이 있는 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("사원이 있는 직급 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<RankQueryDTO>> findRanksWithoutMembers() {
        log.debug("사원이 없는 직급 조회 시작");
        try {
            List<RankQueryDTO> ranks = rankMapper.findRanksWithoutMembers();
            log.debug("사원이 없는 직급 조회 완료. 조회된 직급 수: {}", ranks.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, ranks);
        } catch (Exception e) {
            log.error("사원이 없는 직급 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}