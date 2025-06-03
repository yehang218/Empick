package com.piveguyz.empickbackend.interviewSheet.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="interview_sheet")
public class InterviewSheetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
