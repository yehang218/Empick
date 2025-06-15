package com.piveguyz.empickbackend.orgstructure.job.query.dto;

import com.piveguyz.empickbackend.common.enums.IsActive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 직무 조회용 응답 DTO
 * 직무 조회 결과를 클라이언트에게 전달하는 데이터 전송 객체
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "직무 조회 응답 DTO")
public class JobResponseDTO {

    /**
     * 직무 ID
     */
    @Schema(description = "직무 ID", example = "1")
    private Integer id;

    /**
     * 직무명 (예: 개발자, 디자이너, 기획자)
     */
    @Schema(description = "직무명", example = "백엔드 개발자")
    private String name;

    /**
     * 직무 코드 (예: DEV_001, DES_001, PM_001)
     */
    @Schema(description = "직무 코드", example = "DEV_001")
    private String code;

    /**
     * 활성 여부 (ACTIVE=1, INACTIVE=0)
     */
    @Schema(description = "활성 여부 (1: 활성, 0: 비활성)", example = "1")
    private Integer isActive;

    /**
     * 직무 설명
     */
    @Schema(description = "직무 설명", example = "서버 개발 및 API 설계를 담당합니다.")
    private String description;

    /**
     * 권한 ID (Foreign Key)
     */
    @Schema(description = "권한 ID", example = "101")
    private Integer roleId;

    /**
     * 생성일시
     */
    @Schema(description = "생성일시", example = "2024-01-01T12:00:00")
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    @Schema(description = "수정일시", example = "2024-06-01T09:47:31")
    private LocalDateTime updatedAt;

    // === 조인 데이터 (추가 정보) ===

    /**
     * 권한 코드 (role 테이블 조인)
     */
    @Schema(description = "권한 코드", example = "ROLE_USER")
    private String roleCode;

    /**
     * 권한명 (role 테이블 조인)
     */
    @Schema(description = "권한명", example = "일반 사용자")
    private String roleName;

    /**
     * 해당 직무의 사원 수 (member 테이블 조인)
     */
    @Schema(description = "해당 직무의 사원 수", example = "15")
    private Integer memberCount;

    /**
     * 해당 직무의 활성 사원 수 (status=1인 사원)
     */
    @Schema(description = "해당 직무의 활성 사원 수", example = "12")
    private Integer activeMemberCount;

    // === 비즈니스 메서드 ===

    /**
     * 활성 상태 확인
     */
    public boolean isActiveStatus() {
        return Integer.valueOf(1).equals(isActive);
    }

    /**
     * 비활성 상태 확인
     */
    public boolean isInactiveStatus() {
        return Integer.valueOf(0).equals(isActive);
    }

    /**
     * 설명 존재 여부 확인
     */
    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    /**
     * 권한 존재 여부 확인
     */
    public boolean hasRole() {
        return roleId != null;
    }

    /**
     * 사원이 존재하는지 확인
     */
    public boolean hasMembers() {
        return memberCount != null && memberCount > 0;
    }

    /**
     * 활성 사원이 존재하는지 확인
     */
    public boolean hasActiveMembers() {
        return activeMemberCount != null && activeMemberCount > 0;
    }

    /**
     * 코드 존재 여부 확인
     */
    public boolean hasCode() {
        return code != null && !code.trim().isEmpty();
    }

    /**
     * IsActive enum으로 변환
     */
    public IsActive getIsActiveEnum() {
        return IsActive.fromValue(isActive != null ? isActive : 0);
    }

    /**
     * IsActive enum 설정
     */
    public void setIsActiveEnum(IsActive isActiveEnum) {
        this.isActive = isActiveEnum != null ? isActiveEnum.getValue() : 0;
    }
}