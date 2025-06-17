
package com.piveguyz.empickbackend.orgstructure.position.query.dto;

import com.piveguyz.empickbackend.common.enums.IsActive;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 직책 조회용 DTO
 * MyBatis 조회 결과를 담는 데이터 전송 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PositionQueryDTO {

    /**
     * 직책 ID
     */
    private Integer id;

    /**
     * 직책명 (예: 팀장, 팀원, 부팀장, 본부장, 이사)
     */
    private String name;

    /**
     * 직책 코드 (예: POS_200, TEAM_LEADER)
     */
    private String code;

    /**
     * 활성 여부 (ACTIVE=1, INACTIVE=0)
     */
    private IsActive isActive;

    /**
     * 직책 설명
     */
    private String description;

    /**
     * 권한 ID (Foreign Key)
     */
    private Integer roleId;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    // === 조인 데이터 (추가 정보) ===

    /**
     * 권한 코드 (role 테이블 조인)
     */
    private String roleCode;

    /**
     * 권한명 (role 테이블 조인)
     */
    private String roleName;

    /**
     * 해당 직책의 사원 수 (member 테이블 조인)
     */
    private Integer memberCount;

    /**
     * 해당 직책의 활성 사원 수 (status=1인 사원)
     */
    private Integer activeMemberCount;

    // === 비즈니스 메서드 ===

    /**
     * 활성 상태 확인
     */
    public boolean isActive() {
        return IsActive.ACTIVE.equals(isActive);
    }

    /**
     * 비활성 상태 확인
     */
    public boolean isInactive() {
        return IsActive.INACTIVE.equals(isActive);
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
     * 활성 여부를 정수 값으로 반환 (MyBatis 매핑용)
     */
    public int getIsActiveValue() {
        return isActive != null ? isActive.getValue() : IsActive.INACTIVE.getValue();
    }

    /**
     * 정수 값으로 활성 여부 설정 (MyBatis 매핑용)
     */
    public void setIsActiveValue(int value) {
        this.isActive = IsActive.fromValue(value);
    }
}