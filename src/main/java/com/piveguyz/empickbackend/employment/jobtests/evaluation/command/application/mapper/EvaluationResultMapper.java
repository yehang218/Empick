package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.mapper;


import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.CreateEvaluationResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate.EvaluationResultEntity;

public class EvaluationResultMapper {

    // 평가 결과 DTO -> Entity
    public static EvaluationResultEntity toEntity(CreateEvaluationResultCommandDTO dto) {
        return EvaluationResultEntity.builder()
                .evaluatorComment(dto.getEvaluatorComment())
                .score(dto.getScore())
                .applicationJobtestId(dto.getApplicationJobtestId())
                .jobtestEvaluationCriteriaId(dto.getJobtestEvaluationCriteriaId())
                .build();
    }

    // Entity -> 평가 결과 생성 DTO
    public static CreateEvaluationResultCommandDTO toCreateDto(EvaluationResultEntity entity) {
        return CreateEvaluationResultCommandDTO.builder()
                .evaluatorComment(entity.getEvaluatorComment())
                .score(entity.getScore())
                .applicationJobtestId(entity.getApplicationJobtestId())
                .jobtestEvaluationCriteriaId(entity.getJobtestEvaluationCriteriaId())
                .build();
    }

    // Entity -> 평가 결과 수정 DTO
    public static UpdateEvaluationResultCommandDTO toUpdateDto(EvaluationResultEntity entity) {
        return UpdateEvaluationResultCommandDTO.builder()
                .evaluatorComment(entity.getEvaluatorComment())
                .score(entity.getScore())
                .build();
    }
}
