package com.piveguyz.empickbackend.employment.jobtests.question.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionResponse;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionOptionEntity;

public class QuestionOptionMapper {

    public static QuestionOptionEntity toEntity(CreateQuestionOptionCommandDTO dto, int optionNumber,
                                                int questionId) {
        return QuestionOptionEntity.builder()
                .questionId(questionId)
                .content(dto.getContent())
                .optionNumber(optionNumber)
                .build();
    }

    public static CreateQuestionOptionCommandDTO toCreateDto(QuestionOptionEntity entity) {
        return CreateQuestionOptionCommandDTO.builder()
                .content(entity.getContent())
                .build();
    }

    public static CreateQuestionOptionResponse toCreateResponse(QuestionOptionEntity entity) {
        return CreateQuestionOptionResponse.builder()
                .questionId(entity.getQuestionId())
                .content(entity.getContent())
                .optionNumber(entity.getOptionNumber())
                .build();
    }

    public static UpdateQuestionOptionCommandDTO toUpdateDto(QuestionOptionEntity entity) {
        return UpdateQuestionOptionCommandDTO.builder()
                .content(entity.getContent())
                .optionNumber(entity.getOptionNumber())
                .build();
    }
}
