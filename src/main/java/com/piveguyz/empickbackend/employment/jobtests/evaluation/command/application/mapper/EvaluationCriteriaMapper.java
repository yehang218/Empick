package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.CreateEvaluationCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate.EvaluationCriteriaEntity;

public class EvaluationCriteriaMapper {

    // 평가 기준 DTO -> Entity
    public static EvaluationCriteriaEntity toEntity(CreateEvaluationCriteriaCommandDTO dto) {
        return EvaluationCriteriaEntity.builder()
                .content(dto.getContent())
                .detailContent(dto.getDetailContent())
                .scoreWeight(dto.getScoreWeight())
                .jobTestId(dto.getJobtestId())
                .build();
    }

    // Entity -> 평가 기준 생성 DTO
    public static CreateEvaluationCriteriaCommandDTO toCreateDto(EvaluationCriteriaEntity entity) {
        return CreateEvaluationCriteriaCommandDTO.builder()
                .content(entity.getContent())
                .detailContent(entity.getDetailContent())
                .scoreWeight(entity.getScoreWeight())
                .jobtestId(entity.getJobTestId())
                .build();
    }

    // Entity -> 문제 수정 DTO
    public static UpdateEvaluationCriteriaCommandDTO toUpdateDto(EvaluationCriteriaEntity entity) {
        return UpdateEvaluationCriteriaCommandDTO.builder()
                .content(entity.getContent())
                .detailContent(entity.getDetailContent())
                .scoreWeight(entity.getScoreWeight())
                .build();
    }
}
