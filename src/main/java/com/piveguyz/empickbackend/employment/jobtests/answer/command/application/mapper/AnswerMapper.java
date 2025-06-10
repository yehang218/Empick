package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateAnswerResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateAnswerCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.AnswerEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;

public class AnswerMapper {

    public static AnswerEntity toEntity(CreateAnswerCommandDTO dto, int attempt) {
        return AnswerEntity.builder()
                .content(dto.getContent())
                .attempt(attempt)
                .applicationJobTestId(dto.getApplicationJobTestId())
                .questionId(dto.getQuestionId())
                .build();
    }

    public static CreateAnswerResponseDTO toCreateDTO(AnswerEntity entity) {
        return CreateAnswerResponseDTO.builder()
                .content(entity.getContent())
                .attempt(entity.getAttempt())
                .applicationJobTestId(entity.getApplicationJobTestId())
                .questionId(entity.getQuestionId())
                .build();
    }

    public static UpdateAnswerCommandDTO toUpdateDto(AnswerEntity entity) {
        return UpdateAnswerCommandDTO.builder()
                .isCorrect(entity.getIsCorrect())
                .score(entity.getScore())
                .build();
    }
}
