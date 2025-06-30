package com.piveguyz.empickbackend.orgstructure.position.command.application.dto;

import com.piveguyz.empickbackend.common.enums.IsActive;
import com.piveguyz.empickbackend.orgstructure.position.command.domain.aggregate.PositionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 직책 명령 DTO
 * 직책 생성 및 수정 시 사용되는 데이터 전송 객체
 */
@Schema(description = "직책 명령 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionCommandDTO {

    @Schema(description = "직책명", example = "팀장")
    @Size(max = 255, message = "직책명은 255자를 초과할 수 없습니다.")
    private String name;

    @Schema(description = "직책 코드", example = "TEAM_LEADER")
    @Size(max = 50, message = "직책 코드는 50자를 초과할 수 없습니다.")
    @Pattern(regexp = "^[A-Za-z0-9_]*$", message = "직책 코드는 영문자, 숫자, 언더스코어만 사용할 수 있습니다.")
    private String code;

    @Schema(description = "활성 여부", example = "1")
    private Integer isActive;

    @Schema(description = "직책 설명", example = "팀을 이끄는 리더 역할을 담당합니다.")
    @Size(max = 1000, message = "직책 설명은 1000자를 초과할 수 없습니다.")
    private String description;

    @Schema(description = "권한 ID", example = "1")
    private Integer roleId;

    /**
     * DTO를 엔티티로 변환
     *
     * @return PositionEntity
     */
    public PositionEntity toEntity() {
        return PositionEntity.builder()
                .name(this.name)
                .code(this.code)
                .description(this.description)
                .roleId(this.roleId)
                .build();
    }

    /**
     * 엔티티로부터 DTO 생성
     *
     * @param entity PositionEntity
     * @return PositionCommandDTO
     */
    public static PositionCommandDTO fromEntity(PositionEntity entity) {
        return PositionCommandDTO.builder()
                .name(entity.getName())
                .code(entity.getCode())
                .isActive(entity.getIsActive().getValue())
                .description(entity.getDescription())
                .roleId(entity.getRoleId())
                .build();
    }

    /**
     * 기본 직책 생성용 정적 팩토리 메서드
     *
     * @param name 직책명
     * @param code 직책 코드
     * @return PositionCommandDTO
     */
    public static PositionCommandDTO createBasic(String name, String code) {
        return PositionCommandDTO.builder()
                .name(name)
                .code(code)
                .isActive(IsActive.ACTIVE.getValue())
                .build();
    }

    /**
     * 설명과 함께 직책 생성용 정적 팩토리 메서드
     *
     * @param name 직책명
     * @param code 직책 코드
     * @param description 직책 설명
     * @return PositionCommandDTO
     */
    public static PositionCommandDTO createWithDescription(String name, String code, String description) {
        return PositionCommandDTO.builder()
                .name(name)
                .code(code)
                .isActive(IsActive.ACTIVE.getValue())
                .description(description)
                .build();
    }

    /**
     * 권한과 함께 직책 생성용 정적 팩토리 메서드
     *
     * @param name 직책명
     * @param code 직책 코드
     * @param description 직책 설명
     * @param roleId 권한 ID
     * @return PositionCommandDTO
     */
    public static PositionCommandDTO createWithRole(String name, String code, String description, Integer roleId) {
        return PositionCommandDTO.builder()
                .name(name)
                .code(code)
                .isActive(IsActive.ACTIVE.getValue())
                .description(description)
                .roleId(roleId)
                .build();
    }

    /**
     * IsActive enum 반환
     *
     * @return IsActive
     */
    public IsActive getIsActiveEnum() {
        return IsActive.fromValue(this.isActive);
    }

    /**
     * 설명이 있는지 확인
     *
     * @return 설명 존재 여부
     */
    public boolean hasDescription() {
        return this.description != null && !this.description.trim().isEmpty();
    }

    /**
     * 권한이 연결되어 있는지 확인
     *
     * @return 권한 연결 여부
     */
    public boolean hasRole() {
        return this.roleId != null;
    }

    /**
     * 생성 시 유효성 검증
     *
     * @throws IllegalArgumentException 유효하지 않은 데이터인 경우
     */
    public void validateForCreate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("직책명은 필수입니다.");
        }
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("직책 코드는 필수입니다.");
        }
        validateCommon();
    }

    /**
     * 수정 시 유효성 검증
     *
     * @throws IllegalArgumentException 유효하지 않은 데이터인 경우
     */
    public void validateForUpdate() {
        // 수정 시에는 필수값 검증을 하지 않음 (부분 업데이트 지원)
        validateCommon();
    }

    /**
     * 공통 유효성 검증
     *
     * @throws IllegalArgumentException 유효하지 않은 데이터인 경우
     */
    public void validate() {
        validateCommon();
    }

    /**
     * 공통 유효성 검증 로직
     */
    private void validateCommon() {
        if (name != null && name.trim().length() > 255) {
            throw new IllegalArgumentException("직책명은 255자를 초과할 수 없습니다.");
        }
        if (code != null) {
            if (code.trim().length() > 50) {
                throw new IllegalArgumentException("직책 코드는 50자를 초과할 수 없습니다.");
            }
            if (!code.matches("^[A-Za-z0-9_]*$")) {
                throw new IllegalArgumentException("직책 코드는 영문자, 숫자, 언더스코어만 사용할 수 있습니다.");
            }
        }
        if (description != null && description.length() > 1000) {
            throw new IllegalArgumentException("직책 설명은 1000자를 초과할 수 없습니다.");
        }
        if (isActive != null) {
            // IsActive 유효성 검증
            IsActive.fromValue(this.isActive);
        }
    }

    /**
     * 정규화된 직책명 반환 (앞뒤 공백 제거)
     *
     * @return 정규화된 직책명
     */
    public String getNormalizedName() {
        return this.name != null ? this.name.trim() : null;
    }

    /**
     * 정규화된 직책 코드 반환 (앞뒤 공백 제거, 대문자 변환)
     *
     * @return 정규화된 직책 코드
     */
    public String getNormalizedCode() {
        return this.code != null ? this.code.trim().toUpperCase() : null;
    }

    /**
     * 정규화된 설명 반환 (앞뒤 공백 제거)
     *
     * @return 정규화된 설명
     */
    public String getNormalizedDescription() {
        return this.description != null ? this.description.trim() : null;
    }

    @Override
    public String toString() {
        return "PositionCommandDTO{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isActive=" + isActive +
                ", description='" + description + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}