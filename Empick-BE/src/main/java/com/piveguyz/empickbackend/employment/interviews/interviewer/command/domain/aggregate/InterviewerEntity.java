package com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.aggregate;

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
@Table(name="interviewer")
public class InterviewerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "interview_id")
    private Integer interviewId;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "score")
    private Double score;

    @Column(name = "review")
    private String review;
}
