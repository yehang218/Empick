
package com.piveguyz.empickbackend.orgstructure.rank.query.dto;

import com.piveguyz.empickbackend.common.enums.IsActive;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 직급 조회용 DTO
 * MyBatis 조회 결과를 담는 데이터 전송 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RankQueryDTO {

    /**
     * 직급 ID
     */
    private Integer id;

    /**
     * 직급명 (예: 사원, 대리, 과장, 차장, 부장)
     */
    private String name;

    /**
     * 직급 코드 (예: EMP, AST, MGR, AMG, DIR)
     */
    private String code;

    /**
     * 활성 여부 (ACTIVE=1, INACTIVE=0)
     */
    private IsActive isActive;

    /**
     * 생성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * 급여 밴드 (급여 등급)
     */
    private Integer salaryBand;

    /**
     * 권한 ID (Foreign Key)
     */
    private Integer roleId;

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
     * 해당 직급의 사원 수 (member 테이블 조인)
     */
    private Integer memberCount;

    /**
     * 해당 직급의 활성 사원 수 (status=1인 사원)
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
     * 급여 밴드 존재 여부 확인
     */
    public boolean hasSalaryBand() {
        return salaryBand != null && salaryBand > 0;
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