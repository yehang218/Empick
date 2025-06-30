package com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="interview_score")
public class InterviewScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "interviewer_id")
    private Integer interviewerId;

    @Column(name = "criteria_id")
    private Integer criteriaId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "review")
    private String review;
}
