package com.piveguyz.empickbackend.orgstructure.rank.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.CustomApiResponse;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.orgstructure.rank.command.application.dto.RankCommandDTO;
import com.piveguyz.empickbackend.orgstructure.rank.command.domain.aggregate.RankEntity;
import com.piveguyz.empickbackend.orgstructure.rank.command.domain.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 직급 Command Service 구현체
 * 직급 도메인의 생성, 수정, 삭제 등의 비즈니스 로직을 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RankCommandServiceImpl implements RankCommandService {

    private final RankRepository rankRepository;

    /**
     * 직급 생성
     */
    @Override
    public CustomApiResponse<RankCommandDTO> createRank(RankCommandDTO rankCommandDTO) {
        log.info("직급 생성 요청 - name: {}, code: {}", rankCommandDTO.getName(), rankCommandDTO.getCode());

        try {
            // 코드 정규화 (대문자 변환 및 공백 제거)
            String normalizedCode = normalizeCode(rankCommandDTO.getCode());

            // 중복 코드 검증
            validateDuplicateCode(normalizedCode);

            // Entity 생성
            RankEntity rankEntity = RankEntity.builder()
                    .name(rankCommandDTO.getName().trim())
                    .code(normalizedCode)
                    .isActive(rankCommandDTO.getIsActive())
                    .salaryBand(rankCommandDTO.getSalaryBand())
                    .roleId(rankCommandDTO.getRoleId())
                    .build();

            // 저장
            RankEntity savedEntity = rankRepository.save(rankEntity);

            log.info("직급 생성 완료 - id: {}, name: {}, code: {}", savedEntity.getId(), savedEntity.getName(), savedEntity.getCode());

            RankCommandDTO responseDTO = convertToDTO(savedEntity);
            return CustomApiResponse.of(ResponseCode.CREATED, responseDTO);

        } catch (BusinessException e) {
            log.error("직급 생성 중 비즈니스 오류 발생 - name: {}, code: {}", rankCommandDTO.getName(), rankCommandDTO.getCode(), e);
            throw e;
        } catch (Exception e) {
            log.error("직급 생성 중 예상치 못한 오류 발생 - name: {}, code: {}", rankCommandDTO.getName(), rankCommandDTO.getCode(), e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 직급 수정
     */
    @Override
    public CustomApiResponse<RankCommandDTO> updateRank(Integer id, RankCommandDTO rankCommandDTO) {
        log.info("직급 수정 요청 - id: {}, name: {}, code: {}", id, rankCommandDTO.getName(), rankCommandDTO.getCode());

        try {
            // 존재하는 직급 조회
            RankEntity existingRank = findRankById(id);

            // 코드 정규화
            String normalizedCode = normalizeCode(rankCommandDTO.getCode());

            // 코드 변경 시 중복 검증 (기존 엔티티 제외)
            validateDuplicateCodeForUpdate(normalizedCode, id);

            // 엔티티 업데이트
            existingRank.updateRank(
                    rankCommandDTO.getName().trim(),
                    normalizedCode,
                    rankCommandDTO.getIsActive(),
                    rankCommandDTO.getSalaryBand(),
                    rankCommandDTO.getRoleId()
            );

            RankEntity updatedEntity = rankRepository.save(existingRank);

            log.info("직급 수정 완료 - id: {}, name: {}, code: {}", updatedEntity.getId(), updatedEntity.getName(), updatedEntity.getCode());

            RankCommandDTO responseDTO = convertToDTO(updatedEntity);
            return CustomApiResponse.of(ResponseCode.SUCCESS, responseDTO);

        } catch (BusinessException e) {
            log.error("직급 수정 중 비즈니스 오류 발생 - id: {}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("직급 수정 중 예상치 못한 오류 발생 - id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 직급 삭제
     */
    @Override
    public CustomApiResponse<Void> deleteRank(Integer id) {
        log.info("직급 삭제 요청 - id: {}", id);

        try {
            // 존재하는 직급 조회
            RankEntity existingRank = findRankById(id);

            // TODO: 해당 직급을 사용하는 사원이 있는지 검증 로직 추가
            // validateRankInUse(id);

            // 삭제 실행
            rankRepository.delete(existingRank);

            log.info("직급 삭제 완료 - id: {}, name: {}", id, existingRank.getName());

            return CustomApiResponse.of(ResponseCode.SUCCESS);

        } catch (BusinessException e) {
            log.error("직급 삭제 중 비즈니스 오류 발생 - id: {}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("직급 삭제 중 예상치 못한 오류 발생 - id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 직급 활성 상태 변경
     */
    @Override
    public CustomApiResponse<RankCommandDTO> updateRankStatus(Integer id, IsActive isActive) {
        log.info("직급 상태 변경 요청 - id: {}, isActive: {}", id, isActive);

        try {
            // 존재하는 직급 조회
            RankEntity existingRank = findRankById(id);

            // 상태 업데이트
            existingRank.updateActiveStatus(isActive);

            RankEntity updatedEntity = rankRepository.save(existingRank);

            log.info("직급 상태 변경 완료 - id: {}, name: {}, isActive: {}",
                    updatedEntity.getId(), updatedEntity.getName(), updatedEntity.getIsActive());

            RankCommandDTO responseDTO = convertToDTO(updatedEntity);
            return CustomApiResponse.of(ResponseCode.SUCCESS, responseDTO);

        } catch (BusinessException e) {
            log.error("직급 상태 변경 중 비즈니스 오류 발생 - id: {}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("직급 상태 변경 중 예상치 못한 오류 발생 - id: {}", id, e);
            throw new BusinessException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ID로 직급 조회 (내부 메서드)
     */
    private RankEntity findRankById(Integer id) {
        if (id == null || id <= 0) {
            log.error("잘못된 직급 ID - id: {}", id);
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        return rankRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("직급을 찾을 수 없습니다 - id: {}", id);
                    return new BusinessException(ResponseCode.RANK_NOT_FOUND);
                });
    }

    /**
     * 코드 정규화 (대문자 변환 및 공백 제거)
     */
    private String normalizeCode(String code) {
        if (code == null) {
            return null;
        }
        return code.trim().toUpperCase();
    }

    /**
     * 중복 코드 검증 (생성 시)
     */
    private void validateDuplicateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            log.error("직급 코드가 비어있습니다");
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        boolean exists = rankRepository.existsByCode(code);
        if (exists) {
            log.error("이미 존재하는 직급 코드입니다 - code: {}", code);
            throw new BusinessException(ResponseCode.RANK_CODE_DUPLICATE);
        }

        log.debug("직급 코드 중복 검증 통과 - code: {}", code);
    }

    /**
     * 중복 코드 검증 (수정 시 - 기존 엔티티 제외)
     */
    private void validateDuplicateCodeForUpdate(String code, Integer excludeId) {
        if (code == null || code.trim().isEmpty()) {
            log.error("직급 코드가 비어있습니다");
            throw new BusinessException(ResponseCode.INVALID_PARAMETER);
        }

        // 기존 코드와 같은 경우 검증 생략
        Optional<RankEntity> existingRank = rankRepository.findByCode(code);
        if (existingRank.isPresent() && !existingRank.get().getId().equals(excludeId)) {
            log.error("이미 존재하는 직급 코드입니다 - code: {}", code);
            throw new BusinessException(ResponseCode.RANK_CODE_DUPLICATE);
        }

        log.debug("직급 코드 중복 검증 통과 (수정) - code: {}, excludeId: {}", code, excludeId);
    }

    /**
     * Entity를 DTO로 변환
     */
    private RankCommandDTO convertToDTO(RankEntity entity) {
        RankCommandDTO dto = new RankCommandDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setIsActive(entity.getIsActive());
        dto.setSalaryBand(entity.getSalaryBand());
        dto.setRoleId(entity.getRoleId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}