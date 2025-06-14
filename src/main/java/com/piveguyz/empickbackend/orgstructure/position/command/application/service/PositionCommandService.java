package com.piveguyz.empickbackend.orgstructure.position.command.application.service;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.position.command.application.dto.PositionCommandDTO;
import jakarta.validation.Valid;

/**
 * 직책 명령 서비스 인터페이스
 * 직책 생성, 수정, 삭제 등의 명령 처리를 담당
 */
public interface PositionCommandService {

    /**
     * 직책 등록
     *
     * @param positionCommandDTO 직책 생성 정보
     * @return 생성된 직책 정보
     */
    PositionCommandDTO createPosition(@Valid PositionCommandDTO positionCommandDTO);

    /**
     * 직책 수정
     *
     * @param id 직책 ID
     * @param positionCommandDTO 수정할 직책 정보
     * @return 수정된 직책 정보
     */
    PositionCommandDTO updatePosition(int id, @Valid PositionCommandDTO positionCommandDTO);

    /**
     * 직책 활성 상태 변경
     *
     * @param id 직책 ID
     * @param isActive 변경할 활성 상태
     * @return 수정된 직책 ID
     */
    Integer updatePositionActiveStatus(int id, IsActive isActive);

    /**
     * 직책 삭제 (물리 삭제)
     *
     * @param id 삭제할 직책 ID
     */
    void deletePosition(int id);

    /**
     * 직책에 권한 연결
     *
     * @param id 직책 ID
     * @param roleId 연결할 권한 ID
     * @return 수정된 직책 정보
     */
    PositionCommandDTO connectRole(int id, Integer roleId);

    /**
     * 직책에서 권한 연결 해제
     *
     * @param id 직책 ID
     * @return 수정된 직책 정보
     */
    PositionCommandDTO disconnectRole(int id);

    /**
     * 특정 권한을 사용하는 모든 직책 비활성화
     *
     * @param roleId 권한 ID
     * @return 비활성화된 직책 수
     */
    int deactivatePositionsByRole(Integer roleId);

    /**
     * 특정 권한을 사용하는 모든 직책에서 권한 연결 해제
     *
     * @param roleId 권한 ID
     * @return 권한이 해제된 직책 수
     */
    int disconnectRoleFromAllPositions(Integer roleId);
}