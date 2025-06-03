package com.piveguyz.empickbackend.employment.jobtests.question.query.dto;

import com.piveguyz.empickbackend.employment.jobtests.common.enums.JobtestDifficulty;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.enums.QuestionType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
public class QuestionQueryDTO {
    private int id;
    private String content;
    private String detail_content;
    private QuestionType type;
    private JobtestDifficulty difficulty;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int createdMemberId;
    private int updatedMemberId;

    @Builder
    public QuestionQueryDTO(int id,
                            String content,
                            String detail_content,
                            QuestionType type,
                            JobtestDifficulty difficulty,
                            String answer,
                            LocalDateTime createdAt,
                            LocalDateTime updatedAt,
                            int createdMemberId,
                            int updatedMemberId) {
        this.id = id;
        this.content = content;
        this.detail_content = detail_content;
        this.type = type;
        this.difficulty = difficulty;
        this.answer = answer;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdMemberId = createdMemberId;
        this.updatedMemberId = updatedMemberId;
    }
}
