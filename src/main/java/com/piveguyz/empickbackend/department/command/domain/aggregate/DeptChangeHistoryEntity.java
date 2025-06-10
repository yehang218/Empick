package com.piveguyz.empickbackend.department.command.domain.aggregate;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dept_change_history")
@Getter
@NoArgsConstructor
public class DeptChangeHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "dept_name", nullable = false)
    private String deptName;

    @Column(name = "position_name")
    private String positionName;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "rank_name", nullable = false)
    private String rankName;

    @Column(name = "work_start_at", nullable = false)
    private LocalDateTime workStartAt;

    @Column(name = "work_end_at")
    private LocalDateTime workEndAt;

    // 생성자
    public DeptChangeHistoryEntity(Integer memberId, String deptName, String positionName, String jobName, String rankName, LocalDateTime workStartAt, LocalDateTime workEndAt) {
        this.memberId = memberId;
        this.deptName = deptName;
        this.positionName = positionName;
        this.jobName = jobName;
        this.rankName = rankName;
        this.workStartAt = workStartAt;
        this.workEndAt = workEndAt;
    }

    // 업무 종료일 수정 메서드
    public void endWork(LocalDateTime workEndAt) {
        this.workEndAt = workEndAt;
    }
}
