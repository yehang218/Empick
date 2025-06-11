package com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "recruitment_id", nullable = false)
    private int recruitmentId;

    @Column(name = "applicant_id", nullable = false)
    private int applicantId;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "introduce_rating_result_id")
    private Integer introduceRatingResultId;

}
