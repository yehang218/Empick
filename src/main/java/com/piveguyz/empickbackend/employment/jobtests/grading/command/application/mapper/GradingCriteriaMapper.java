package com.piveguyz.empickbackend.employment.jobtests.grading.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.CreateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.UpdateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.aggregate.GradingCriteriaEntity;

public class GradingCriteriaMapper {

    public static GradingCriteriaEntity toEntity(CreateGradingCriteriaCommandDTO dto, int questionId) {
        return GradingCriteriaEntity.builder()
                .content(dto.getContent())
                .detailContent(dto.getDetailContent())
                .scoreWeight(dto.getScoreWeight())
                .questionId(questionId)
                .build();
    }

    public static CreateGradingCriteriaCommandDTO toCreateDto(GradingCriteriaEntity entity) {
        return CreateGradingCriteriaCommandDTO.builder()
                .content(entity.getContent())
                .detailContent(entity.getDetailContent())
                .scoreWeight(entity.getScoreWeight())
                .build();
    }

    public static UpdateGradingCriteriaCommandDTO toUpdateDto(GradingCriteriaEntity entity) {
        return UpdateGradingCriteriaCommandDTO.builder()
                .content(entity.getContent())
                .detailContent(entity.getDetailContent())
                .scoreWeight(entity.getScoreWeight())
                .build();
    }
}
