package com.piveguyz.empickbackend.employment.interview.command.domain.aggregate;

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
@Table(name="interview")
public class InterviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    @Column(name = "id")
    private Integer id;

    @Column(name = "applicant_id")
    private Integer applicantId;

    @Column(name = "sheet_id")
    private Integer sheetId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "address")
    private String address;

    @Column(name = "score")
    private Double score;

    @Column(name = "interview_review")
    private String interviewReview;
}
