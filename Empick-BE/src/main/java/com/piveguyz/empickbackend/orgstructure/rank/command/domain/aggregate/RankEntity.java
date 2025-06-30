package com.piveguyz.empickbackend.orgstructure.rank.command.domain.aggregate;

import com.piveguyz.empickbackend.common.enums.IsActive;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 직급 엔티티
 * 조직 구조 내 직급 정보를 관리하는 도메인 엔티티
 */
@Entity
@Table(name = "`rank`")  // 백틱으로 예약어 처리
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)  // 코드는 유니크해야 함
    private String code;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private IsActive isActive;

    @Column(name = "salary_band")
    private Integer salaryBand;

    @Column(name = "role_id")
    private Integer roleId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 직급 정보 업데이트
     */
    public void updateRank(String name, String code, IsActive isActive,
                           Integer salaryBand, Integer roleId) {
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.salaryBand = salaryBand;
        this.roleId = roleId;
    }

    /**
     * 활성 상태 변경
     */
    public void updateActiveStatus(IsActive isActive) {
        this.isActive = isActive;
    }

    /**
     * 권한 ID 업데이트
     */
    public void updateRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * 급여 밴드 업데이트
     */
    public void updateSalaryBand(Integer salaryBand) {
        this.salaryBand = salaryBand;
    }

    /**
     * 활성 상태 확인
     */
    public boolean isActive() {
        return IsActive.ACTIVE.equals(this.isActive);
    }

    /**
     * 비활성 상태 확인
     */
    public boolean isInactive() {
        return IsActive.INACTIVE.equals(this.isActive);
    }
}