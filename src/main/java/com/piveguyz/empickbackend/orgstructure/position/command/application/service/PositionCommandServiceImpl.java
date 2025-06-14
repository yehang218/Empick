package com.piveguyz.empickbackend.orgstructure.position.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.position.command.application.dto.PositionCommandDTO;
import com.piveguyz.empickbackend.orgstructure.position.command.domain.aggregate.PositionEntity;
import com.piveguyz.empickbackend.orgstructure.position.command.domain.repository.PositionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 직책 명령 서비스 구현체
 * 직책 관련 비즈니스 로직 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PositionCommandServiceImpl implements PositionCommandService {

    private final PositionRepository positionRepository;

    @Override
    @Transactional
    public PositionCommandDTO createPosition(@Valid PositionCommandDTO positionCommandDTO) {
        log.info("직책 생성 요청: {}", positionCommandDTO);

        // 비즈니스 검증
        validatePositionForCreate(positionCommandDTO);

        // 엔티티 생성
        PositionEntity positionEntity = PositionEntity.builder()
                .name(positionCommandDTO.getName())
                .code(positionCommandDTO.getCode())
                .description(positionCommandDTO.getDescription())
                .roleId(positionCommandDTO.getRoleId())
                .build();

        // 저장
        PositionEntity savedPosition = positionRepository.save(positionEntity);

        log.info("직책 생성 완료: ID={}", savedPosition.getId());
        return toDTO(savedPosition);
    }

    @Override
    @Transactional
    public PositionCommandDTO updatePosition(int id, @Valid PositionCommandDTO positionCommandDTO) {
        log.info("직책 수정 요청: ID={}, Data={}", id, positionCommandDTO);

        // 엔티티 조회
        PositionEntity position = findPositionById(id);

        // 비즈니스 검증
        validatePositionForUpdate(id, positionCommandDTO);

        // 엔티티 업데이트
        position.updatePosition(
                positionCommandDTO.getName(),
                positionCommandDTO.getCode(),
                positionCommandDTO.getDescription(),
                positionCommandDTO.getRoleId()
        );

        // 활성 상태 설정
        if (positionCommandDTO.getIsActive() != null) {
            position.setActiveStatus(IsActive.fromValue(positionCommandDTO.getIsActive()));
        }

        // 저장
        PositionEntity updatedPosition = positionRepository.save(position);

        log.info("직책 수정 완료: ID={}", updatedPosition.getId());
        return toDTO(updatedPosition);
    }

    @Override
    @Transactional
    public Integer updatePositionActiveStatus(int id, IsActive isActive) {
        log.info("직책 활성 상태 변경 요청: ID={}, IsActive={}", id, isActive);

        // 엔티티 조회
        PositionEntity position = findPositionById(id);

        // 활성 상태 변경
        position.setActiveStatus(isActive);

        // 저장
        positionRepository.save(position);

        log.info("직책 활성 상태 변경 완료: ID={}, IsActive={}", id, isActive);
        return position.getId();
    }

    @Override
    @Transactional
    public void deletePosition(int id) {
        log.info("직책 삭제 요청: ID={}", id);

        // 엔티티 조회
        PositionEntity position = findPositionById(id);

        // 삭제 전 검증
        validatePositionForDelete(position);

        // 삭제
        positionRepository.delete(position);

        log.info("직책 삭제 완료: ID={}", id);
    }

    @Override
    @Transactional
    public PositionCommandDTO connectRole(int id, Integer roleId) {
        log.info("직책 권한 연결 요청: PositionID={}, RoleID={}", id, roleId);

        // 엔티티 조회
        PositionEntity position = findPositionById(id);

        // 권한 연결
        position.connectRole(roleId);

        // 저장
        PositionEntity updatedPosition = positionRepository.save(position);

        log.info("직책 권한 연결 완료: PositionID={}, RoleID={}", id, roleId);
        return toDTO(updatedPosition);
    }

    @Override
    @Transactional
    public PositionCommandDTO disconnectRole(int id) {
        log.info("직책 권한 해제 요청: PositionID={}", id);

        // 엔티티 조회
        PositionEntity position = findPositionById(id);

        // 권한 해제
        position.disconnectRole();

        // 저장
        PositionEntity updatedPosition = positionRepository.save(position);

        log.info("직책 권한 해제 완료: PositionID={}", id);
        return toDTO(updatedPosition);
    }

    @Override
    @Transactional
    public int deactivatePositionsByRole(Integer roleId) {
        log.info("권한별 직책 일괄 비활성화 요청: RoleID={}", roleId);

        // 벌크 업데이트
        int updatedCount = positionRepository.updateIsActiveByRoleId(roleId, IsActive.INACTIVE);

        log.info("권한별 직책 일괄 비활성화 완료: RoleID={}, Count={}", roleId, updatedCount);
        return updatedCount;
    }

    @Override
    @Transactional
    public int disconnectRoleFromAllPositions(Integer roleId) {
        log.info("전체 직책에서 권한 연결 해제 요청: RoleID={}", roleId);

        // 벌크 업데이트
        int updatedCount = positionRepository.disconnectRole(roleId);

        log.info("전체 직책에서 권한 연결 해제 완료: RoleID={}, Count={}", roleId, updatedCount);
        return updatedCount;
    }

    /**
     * ID로 직책 엔티티 조회
     *
     * @param id 직책 ID
     * @return PositionEntity
     * @throws IllegalArgumentException 직책을 찾을 수 없는 경우
     */
    private PositionEntity findPositionById(int id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("직책을 찾을 수 없습니다. ID: " + id));
    }

    /**
     * 직책 생성 시 유효성 검증
     *
     * @param positionCommandDTO 검증할 직책 정보
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validatePositionForCreate(PositionCommandDTO positionCommandDTO) {
        // 직책명 중복 검증
        if (positionRepository.existsByName(positionCommandDTO.getName())) {
            throw new IllegalArgumentException("이미 존재하는 직책명입니다: " + positionCommandDTO.getName());
        }

        // 직책 코드 중복 검증
        if (positionRepository.existsByCode(positionCommandDTO.getCode())) {
            throw new IllegalArgumentException("이미 존재하는 직책 코드입니다: " + positionCommandDTO.getCode());
        }

        // 추가 비즈니스 검증
        positionCommandDTO.validate();
    }

    /**
     * 직책 수정 시 유효성 검증
     *
     * @param id 직책 ID
     * @param positionCommandDTO 검증할 직책 정보
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validatePositionForUpdate(int id, PositionCommandDTO positionCommandDTO) {
        // 직책명 중복 검증 (자기 제외)
        if (positionCommandDTO.getName() != null &&
                positionRepository.existsByNameAndIdNot(positionCommandDTO.getName(), id)) {
            throw new IllegalArgumentException("이미 존재하는 직책명입니다: " + positionCommandDTO.getName());
        }

        // 직책 코드 중복 검증 (자기 제외)
        if (positionCommandDTO.getCode() != null &&
                positionRepository.existsByCodeAndIdNot(positionCommandDTO.getCode(), id)) {
            throw new IllegalArgumentException("이미 존재하는 직책 코드입니다: " + positionCommandDTO.getCode());
        }

        // 추가 비즈니스 검증
        positionCommandDTO.validate();
    }

    /**
     * 직책 삭제 시 유효성 검증
     *
     * @param position 삭제할 직책 엔티티
     * @throws IllegalArgumentException 삭제할 수 없는 경우
     */
    private void validatePositionForDelete(PositionEntity position) {
        // 활성 상태인 직책은 삭제 불가
        if (position.isActive()) {
            throw new IllegalArgumentException("활성 상태인 직책은 삭제할 수 없습니다. 먼저 비활성화해주세요.");
        }

        // TODO: 직책을 사용하는 직원이 있는지 확인
        // if (employeeRepository.existsByPositionId(position.getId())) {
        //     throw new IllegalArgumentException("직책을 사용하는 직원이 있어 삭제할 수 없습니다.");
        // }
    }

    /**
     * Entity를 DTO로 변환
     *
     * @param position PositionEntity
     * @return PositionCommandDTO
     */
    private PositionCommandDTO toDTO(PositionEntity position) {
        return PositionCommandDTO.builder()
                .name(position.getName())
                .code(position.getCode())
                .isActive(position.getIsActive().getValue())
                .description(position.getDescription())
                .roleId(position.getRoleId())
                .build();
    }
}