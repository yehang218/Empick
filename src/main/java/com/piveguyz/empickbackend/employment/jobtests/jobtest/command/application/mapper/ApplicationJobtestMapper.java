package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateApplicationJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.ApplicationJobtestEntity;

public class ApplicationJobtestMapper {

    public static ApplicationJobtestEntity toEntity(CreateApplicationJobtestCommandDTO dto) {
        return ApplicationJobtestEntity.builder()
                .evaluatorComment(dto.getEvaluatorComment())
                .submittedAt(dto.getSubmittedAt())
                .gradingTotalScore(dto.getGradingTotalScore())
                .evaluationScore(dto.getEvaluationScore())
                .evaluationStatus(dto.getEvaluationStatus())
                .gradingStatus(dto.getGradingStatus())
                .entryCode(dto.getEntryCode())
                .applicationId(dto.getApplicationId())
                .jobTestId(dto.getJobtestId())
                .memberId(dto.getMemberId())
                .build();
    }

    public static CreateApplicationJobtestCommandDTO toCreateDto(ApplicationJobtestEntity entity) {
        return CreateApplicationJobtestCommandDTO.builder()
                .evaluatorComment(entity.getEvaluatorComment())
                .evaluationScore(entity.getEvaluationScore())
                .submittedAt(entity.getSubmittedAt())
                .gradingTotalScore(entity.getGradingTotalScore())
                .evaluationScore(entity.getEvaluationScore())
                .gradingStatus(entity.getGradingStatus())
                .evaluationStatus(entity.getEvaluationStatus())
                .entryCode(entity.getEntryCode())
                .applicationId(entity.getApplicationId())
                .jobtestId(entity.getJobTestId())
                .memberId(entity.getMemberId())
                .build();
    }

    public static UpdateApplicationJobtestCommandDTO toUpdateDto(ApplicationJobtestEntity entity) {
        return UpdateApplicationJobtestCommandDTO.builder()
                .evaluatorComment(entity.getEvaluatorComment())
                .submittedAt(entity.getSubmittedAt())
                .gradingTotalScore(entity.getGradingTotalScore())
                .evaluationScore(entity.getEvaluationScore())
                .gradingStatus(entity.getGradingStatus())
                .evaluationStatus(entity.getEvaluationStatus())
                .entryCode(entity.getEntryCode())
                .memberId(entity.getMemberId())
                .build();
    }
}
