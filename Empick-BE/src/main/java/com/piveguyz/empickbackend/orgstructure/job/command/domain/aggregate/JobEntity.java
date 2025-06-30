package com.piveguyz.empickbackend.orgstructure.job.command.domain.aggregate;

import com.piveguyz.empickbackend.common.enums.IsActive;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job")
@Getter
@Setter
@NoArgsConstructor
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private IsActive isActive;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "role_id")
    private Integer roleId;

    // JPA가 자동으로 생성 시간, 수정 시간을 주입하도록 함
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public JobEntity(Integer id, String name, String code, IsActive isActive, String description,
                     Integer roleId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.isActive = isActive;
        this.description = description;
        this.roleId = roleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}