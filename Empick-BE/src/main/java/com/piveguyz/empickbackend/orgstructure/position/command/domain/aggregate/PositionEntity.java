package com.piveguyz.empickbackend.orgstructure.position.command.domain.aggregate;

import com.piveguyz.empickbackend.common.enums.IsActive;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 직책 엔티티
 * 조직 내 직책 정보를 관리하는 도메인 엔티티
 *
 * @author EmpickTeam
 * @since 1.0
 */
@Entity
@Table(
        name = "position",
        indexes = {
                @Index(name = "idx_position_name", columnList = "name"),
                @Index(name = "idx_position_code", columnList = "code"),
                @Index(name = "idx_position_is_active", columnList = "is_active"),
                @Index(name = "idx_position_role_id", columnList = "role_id"),
                @Index(name = "idx_position_name_active", columnList = "name, is_active"),
                @Index(name = "idx_position_code_active", columnList = "code, is_active")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_position_name", columnNames = "name"),
                @UniqueConstraint(name = "uk_position_code", columnNames = "code")
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("직책 정보 테이블")
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("직책 고유 식별자")
    private Integer id;

    @Column(name = "name", nullable = false, length = 255)
    @Comment("직책명")
    private String name;

    @Column(name = "code", nullable = false, length = 50)
    @Comment("직책 코드")
    private String code;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_active", nullable = false)
    @Comment("활성 여부 (1: 활성, 0: 비활성)")
    private IsActive isActive;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("직책 설명")
    private String description;

    @Column(name = "role_id")
    @Comment("연결된 권한 ID")
    private Integer roleId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 일시")
    private LocalDateTime updatedAt;

    /**
     * 빌더 패턴을 통한 PositionEntity 생성
     *
     * @param name 직책명
     * @param code 직책 코드
     * @param description 직책 설명
     * @param roleId 권한 ID
     */
    @Builder
    private PositionEntity(String name, String code, String description, Integer roleId) {
        validateName(name);
        validateCode(code);
        this.name = name;
        this.code = code;
        this.description = description;
        this.roleId = roleId;
        this.isActive = IsActive.ACTIVE; // 기본값은 활성
    }

    /**
     * 직책 정보 업데이트
     *
     * @param name 새로운 직책명
     * @param code 새로운 직책 코드
     * @param description 새로운 설명
     * @param roleId 새로운 권한 ID
     */
    public void updatePosition(String name, String code, String description, Integer roleId) {
        if (name != null) {
            validateName(name);
            this.name = name;
        }
        if (code != null) {
            validateCode(code);
            this.code = code;
        }
        this.description = description;
        this.roleId = roleId;
    }

    /**
     * 직책명만 업데이트
     *
     * @param name 새로운 직책명
     */
    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    /**
     * 직책 코드만 업데이트
     *
     * @param code 새로운 직책 코드
     */
    public void updateCode(String code) {
        validateCode(code);
        this.code = code;
    }

    /**
     * 직책 설명 업데이트
     *
     * @param description 새로운 설명
     */
    public void updateDescription(String description) {
        this.description = description;
    }

    /**
     * 권한 연결
     *
     * @param roleId 연결할 권한 ID
     */
    public void connectRole(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 권한 연결 해제
     */
    public void disconnectRole() {
        this.roleId = null;
    }

    /**
     * 직책 활성화
     */
    public void activate() {
        this.isActive = IsActive.ACTIVE;
    }

    /**
     * 직책 비활성화
     */
    public void deactivate() {
        this.isActive = IsActive.INACTIVE;
    }

    /**
     * 활성 상태 설정
     *
     * @param isActive 활성 상태
     */
    public void setActiveStatus(IsActive isActive) {
        this.isActive = isActive;
    }

    /**
     * 활성 상태 토글
     */
    public void toggleActiveStatus() {
        this.isActive = this.isActive == IsActive.ACTIVE ? IsActive.INACTIVE : IsActive.ACTIVE;
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
     * 설명이 있는지 확인
     *
     * @return 설명 존재 여부
     */
    public boolean hasDescription() {
        return this.description != null && !this.description.trim().isEmpty();
    }

    /**
     * 활성 상태인지 확인
     *
     * @return 활성 여부
     */
    public boolean isActive() {
        return this.isActive == IsActive.ACTIVE;
    }

    /**
     * 비활성 상태인지 확인
     *
     * @return 비활성 여부
     */
    public boolean isInactive() {
        return this.isActive == IsActive.INACTIVE;
    }

    /**
     * 직책명 유효성 검증
     *
     * @param name 검증할 직책명
     * @throws IllegalArgumentException 유효하지 않은 직책명인 경우
     */
    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("직책명은 필수입니다.");
        }
        if (name.trim().length() > 255) {
            throw new IllegalArgumentException("직책명은 255자를 초과할 수 없습니다.");
        }
    }

    /**
     * 직책 코드 유효성 검증
     *
     * @param code 검증할 직책 코드
     * @throws IllegalArgumentException 유효하지 않은 직책 코드인 경우
     */
    private void validateCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("직책 코드는 필수입니다.");
        }
        if (code.trim().length() > 50) {
            throw new IllegalArgumentException("직책 코드는 50자를 초과할 수 없습니다.");
        }
        // 코드는 영문자, 숫자, 언더스코어만 허용
        if (!code.matches("^[A-Za-z0-9_]+$")) {
            throw new IllegalArgumentException("직책 코드는 영문자, 숫자, 언더스코어만 사용할 수 있습니다.");
        }
    }

    /**
     * 정적 팩토리 메서드 - 기본 직책 생성
     *
     * @param name 직책명
     * @param code 직책 코드
     * @return PositionEntity
     */
    public static PositionEntity createPosition(String name, String code) {
        return PositionEntity.builder()
                .name(name)
                .code(code)
                .build();
    }

    /**
     * 정적 팩토리 메서드 - 설명과 함께 직책 생성
     *
     * @param name 직책명
     * @param code 직책 코드
     * @param description 직책 설명
     * @return PositionEntity
     */
    public static PositionEntity createPositionWithDescription(String name, String code, String description) {
        return PositionEntity.builder()
                .name(name)
                .code(code)
                .description(description)
                .build();
    }

    /**
     * 정적 팩토리 메서드 - 권한과 함께 직책 생성
     *
     * @param name 직책명
     * @param code 직책 코드
     * @param description 직책 설명
     * @param roleId 권한 ID
     * @return PositionEntity
     */
    public static PositionEntity createPositionWithRole(String name, String code, String description, Integer roleId) {
        return PositionEntity.builder()
                .name(name)
                .code(code)
                .description(description)
                .roleId(roleId)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PositionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isActive=" + isActive +
                ", description='" + description + '\'' +
                ", roleId=" + roleId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    /**
     * 엔티티 복사본 생성 (ID 제외)
     *
     * @return 복사된 PositionEntity
     */
    public PositionEntity copy() {
        return PositionEntity.builder()
                .name(this.name)
                .code(this.code)
                .description(this.description)
                .roleId(this.roleId)
                .build();
    }

    /**
     * 엔티티 요약 정보 반환
     *
     * @return 요약 문자열
     */
    public String getSummary() {
        return String.format("Position[id=%d, name='%s', code='%s', active=%s, hasRole=%s]",
                id, name, code, isActive, hasRole());
    }
}