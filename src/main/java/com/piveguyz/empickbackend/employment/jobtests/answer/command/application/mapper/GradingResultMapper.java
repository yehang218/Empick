package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateGradingResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateGradingResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.GradingResultEntity;

public class GradingResultMapper {

    public static GradingResultEntity toEntity(CreateGradingResultCommandDTO dto) {
        return GradingResultEntity.builder()
                .evaluatorComment(dto.getEvaluatorComment())
                .isSatisfied(dto.getIsSatisfied())
                .answerId(dto.getAnswerId())
                .questionGradingCriteriaId(dto.getQuestionGradingCriteriaId())
                .build();
    }

    public static CreateGradingResultCommandDTO toCommandDTO(GradingResultEntity entity) {
        return CreateGradingResultCommandDTO.builder()
                .evaluatorComment(entity.getEvaluatorComment())
                .isSatisfied(entity.getIsSatisfied())
                .answerId(entity.getAnswerId())
                .questionGradingCriteriaId(entity.getQuestionGradingCriteriaId())
                .build();
    }

    public static UpdateGradingResultCommandDTO toUpdateDto(GradingResultEntity entity) {
        return UpdateGradingResultCommandDTO.builder()
                .evaluatorComment(entity.getEvaluatorComment())
                .isSatisfied(entity.getIsSatisfied())
                .build();
    }
}
