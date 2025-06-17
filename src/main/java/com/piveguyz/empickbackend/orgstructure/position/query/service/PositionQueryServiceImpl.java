
package com.piveguyz.empickbackend.orgstructure.position.query.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.position.query.dto.PositionQueryDTO;
import com.piveguyz.empickbackend.orgstructure.position.query.mapper.PositionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 직책 조회 서비스 구현체
 * MyBatis 기반 복잡한 조회 로직 처리 및 CustomApiResponse 반환
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PositionQueryServiceImpl implements PositionQueryService {

    private final PositionMapper positionMapper;

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findAll() {
        log.debug("모든 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findAll();
            log.debug("모든 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("모든 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findByIsActive(IsActive isActive) {
        log.debug("활성 여부별 직책 조회 시작. isActive: {}", isActive);
        try {
            List<PositionQueryDTO> positions = positionMapper.findByIsActive(isActive.getValue());
            log.debug("활성 여부별 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("활성 여부별 직책 조회 중 오류 발생. isActive: {}", isActive, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findActivePositions() {
        log.debug("활성 직책 조회 시작");
        return findByIsActive(IsActive.ACTIVE);
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findInactivePositions() {
        log.debug("비활성 직책 조회 시작");
        return findByIsActive(IsActive.INACTIVE);
    }

    @Override
    public CustomApiResponse<PositionQueryDTO> findById(Integer id) {
        log.debug("ID로 직책 조회 시작. id: {}", id);

        if (id == null || id <= 0) {
            log.warn("잘못된 직책 ID: {}", id);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            PositionQueryDTO position = positionMapper.findById(id);
            if (position == null) {
                log.debug("ID로 직책 조회 결과 없음. id: {}", id);
                throw new BusinessException(ResponseCode.POSITION_NOT_FOUND);
            }

            log.debug("ID로 직책 조회 완료. id: {}", id);
            return CustomApiResponse.of(ResponseCode.SUCCESS, position);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("ID로 직책 조회 중 오류 발생. id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<PositionQueryDTO> findByCode(String code) {
        log.debug("코드로 직책 조회 시작. code: {}", code);

        if (code == null || code.trim().isEmpty()) {
            log.warn("잘못된 직책 코드: {}", code);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            PositionQueryDTO position = positionMapper.findByCode(code.trim());
            if (position == null) {
                log.debug("코드로 직책 조회 결과 없음. code: {}", code);
                throw new BusinessException(ResponseCode.POSITION_NOT_FOUND);
            }

            log.debug("코드로 직책 조회 완료. code: {}", code);
            return CustomApiResponse.of(ResponseCode.SUCCESS, position);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("코드로 직책 조회 중 오류 발생. code: {}", code, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<PositionQueryDTO> findByName(String name) {
        log.debug("이름으로 직책 조회 시작. name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            log.warn("잘못된 직책명: {}", name);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            PositionQueryDTO position = positionMapper.findByName(name.trim());
            if (position == null) {
                log.debug("이름으로 직책 조회 결과 없음. name: {}", name);
                throw new BusinessException(ResponseCode.POSITION_NOT_FOUND);
            }

            log.debug("이름으로 직책 조회 완료. name: {}", name);
            return CustomApiResponse.of(ResponseCode.SUCCESS, position);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("이름으로 직책 조회 중 오류 발생. name: {}", name, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findWithRoleInfo() {
        log.debug("권한 정보 포함 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findWithRoleInfo();
            log.debug("권한 정보 포함 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("권한 정보 포함 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findWithMemberCount() {
        log.debug("사원 수 정보 포함 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findWithMemberCount();
            log.debug("사원 수 정보 포함 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("사원 수 정보 포함 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findWithFullDetails() {
        log.debug("상세 정보 포함 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findWithFullDetails();
            log.debug("상세 정보 포함 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("상세 정보 포함 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findByRoleId(Integer roleId) {
        log.debug("권한 ID별 직책 조회 시작. roleId: {}", roleId);

        if (roleId == null || roleId <= 0) {
            log.warn("잘못된 권한 ID: {}", roleId);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<PositionQueryDTO> positions = positionMapper.findByRoleId(roleId);
            log.debug("권한 ID별 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("권한 ID별 직책 조회 중 오류 발생. roleId: {}", roleId, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findByNameContaining(String keyword) {
        log.debug("키워드로 직책 검색 시작. keyword: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("검색 키워드가 비어있음: {}", keyword);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<PositionQueryDTO> positions = positionMapper.findByNameContaining(keyword.trim());
            log.debug("키워드로 직책 검색 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("키워드로 직책 검색 중 오류 발생. keyword: {}", keyword, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findByCodeContaining(String keyword) {
        log.debug("코드 키워드로 직책 검색 시작. keyword: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("검색 키워드가 비어있음: {}", keyword);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        try {
            List<PositionQueryDTO> positions = positionMapper.findByCodeContaining(keyword.trim());
            log.debug("코드 키워드로 직책 검색 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("코드 키워드로 직책 검색 중 오류 발생. keyword: {}", keyword, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findPositionsWithDescription() {
        log.debug("설명이 있는 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findPositionsWithDescription();
            log.debug("설명이 있는 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("설명이 있는 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findPositionsWithoutDescription() {
        log.debug("설명이 없는 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findPositionsWithoutDescription();
            log.debug("설명이 없는 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("설명이 없는 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findPositionsWithMembers() {
        log.debug("사원이 있는 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findPositionsWithMembers();
            log.debug("사원이 있는 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("사원이 있는 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findPositionsWithoutMembers() {
        log.debug("사원이 없는 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findPositionsWithoutMembers();
            log.debug("사원이 없는 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("사원이 없는 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findPositionsWithRole() {
        log.debug("권한이 연결된 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findPositionsWithRole();
            log.debug("권한이 연결된 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("권한이 연결된 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomApiResponse<List<PositionQueryDTO>> findPositionsWithoutRole() {
        log.debug("권한이 연결되지 않은 직책 조회 시작");
        try {
            List<PositionQueryDTO> positions = positionMapper.findPositionsWithoutRole();
            log.debug("권한이 연결되지 않은 직책 조회 완료. 조회된 직책 수: {}", positions.size());
            return CustomApiResponse.of(ResponseCode.SUCCESS, positions);
        } catch (Exception e) {
            log.error("권한이 연결되지 않은 직책 조회 중 오류 발생", e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}