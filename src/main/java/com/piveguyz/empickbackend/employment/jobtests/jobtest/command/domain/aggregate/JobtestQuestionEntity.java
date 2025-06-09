package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestQuestionCommandDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "job_test_question")
public class JobtestQuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "option_number", nullable = false)
    private int optionNumber;

    @Column(name = "job_test_id", nullable = false)
    private int jobTestId;

    @Column(name = "question_id", nullable = false)
    private int questionId;

    public void updateJobtestQuestionEntity(UpdateJobtestQuestionCommandDTO dto) {
        if(dto.getScore() != null) {
            this.score = dto.getScore();
        }
        if(dto.getOptionNumber() != null) {
            this.optionNumber = dto.getOptionNumber();
        }
    }
}
