package com.piveguyz.empickbackend.interviewScore.command.domain.aggregate;

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
@Table(name="interview_score")
public class InterviewScoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "interview_id")
    private Integer interviewId;

    @Column(name = "interviewer_id")
    private Integer interviewerId;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "score")
    private Double score;

    @Column(name = "review")
    private String review;
}
