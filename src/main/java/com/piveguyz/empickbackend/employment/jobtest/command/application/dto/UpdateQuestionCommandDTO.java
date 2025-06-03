package com.piveguyz.empickbackend.employment.jobtest.command.application.dto;

import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums.JobtestDifficulty;
import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.enums.JobtestType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UpdateQuestionCommandDTO {
    private int id;
    private String content;
    private String detailContent;
    private JobtestType type;
    private JobtestDifficulty difficulty;
    private String answer;
    private int updatedMemberId;

    @Builder
    public UpdateQuestionCommandDTO(int id, String content, String detailContent, JobtestType type,
                                    JobtestDifficulty difficulty, String answer, int updatedMemberId) {
        this.id = id;
        this.content = content;
        this.detailContent = detailContent;
        this.type = type;
        this.difficulty = difficulty;
        this.answer = answer;
        this.updatedMemberId = updatedMemberId;
    }
}
