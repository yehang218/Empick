package com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate;

import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionOptionCommandDTO;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Table(name = "question_option")
public class QuestionOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "option_number", nullable = false)
    private int optionNumber;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "question_id", nullable = false)
    private int questionId;

    public void updateQuestionOptionEntity(UpdateQuestionOptionCommandDTO dto) {
        if (dto.getOptionNumber() != null) {
            this.optionNumber = dto.getOptionNumber();
        }
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }
    }
}
