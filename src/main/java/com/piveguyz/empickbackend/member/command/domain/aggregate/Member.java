package com.piveguyz.empickbackend.member.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "employee_number", nullable = false)
    private int employeeNumber;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "birth", length = 255)
    private String birth;

    @Column(name = "phone", nullable = false, length = 255)
    private String phone;

    @Column(name = "picture_url", nullable = false, length = 255)
    private String pictureUrl;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "vacation_count", nullable = false)
    private int vacationCount;

    @Column(name = "hire_at", nullable = false)
    private LocalDateTime hireAt;

    @Column(name = "resign_at")
    private LocalDateTime resignAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_member_id")
    private Integer createdMemberId;

    @Column(name = "deleted_member_id")
    private Integer deletedMemberId;

    @Column(name = "updated_member_id")
    private Integer updatedMemberId;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "position_id")
    private Integer positionId;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "rank_id")
    private Integer rankId;
}
