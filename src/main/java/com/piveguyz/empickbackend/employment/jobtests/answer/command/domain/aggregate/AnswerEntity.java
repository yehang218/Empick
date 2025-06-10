package com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.enums.CorrectType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "answer")
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "attempt", nullable = false)
    private int attempt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "is_correct", nullable = true)
    private CorrectType isCorrect;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "application_job_test_id", nullable = false)
    private int applicationJobTestId;

    @Column(name = "question_id", nullable = false)
    private int questionId;

    public void applyGradingResult(CorrectType correctType, double score) {
        this.isCorrect = correctType;
        this.score = score;
    }

    public void updateAnswerEntity(UpdateAnswerCommandDTO dto) {
        this.isCorrect = dto.getIsCorrect();
        this.score = dto.getScore();
    }

    public void updateAnswerEntity(CreateAnswerCommandDTO dto, int attempt) {
        this.content = dto.getContent();
        this.attempt = attempt;
    }
}
