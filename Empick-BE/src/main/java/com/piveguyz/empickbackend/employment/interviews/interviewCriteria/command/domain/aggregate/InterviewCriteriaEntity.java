package com.piveguyz.empickbackend.employment.interviews.interviewCriteria.command.domain.aggregate;

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
@Table(name="interview_criteria")
public class InterviewCriteriaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "sheet_id")
    private Integer sheetId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
