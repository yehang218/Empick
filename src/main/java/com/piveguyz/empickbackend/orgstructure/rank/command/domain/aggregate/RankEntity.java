package com.piveguyz.empickbackend.orgstructure.rank.command.domain.aggregate;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rank")
@Getter
@NoArgsConstructor
public class RankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(name = "salary_band")
    private Integer salaryBand;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.LocalDateTime updatedAt;
}
