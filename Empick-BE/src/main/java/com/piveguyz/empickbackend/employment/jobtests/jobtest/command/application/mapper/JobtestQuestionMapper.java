package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestQuestionResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestQuestionEntity;

public class JobtestQuestionMapper {

    public static JobtestQuestionEntity toEntity(CreateJobtestQuestionCommandDTO dto, int optionNumber, int jobtestId) {
        return JobtestQuestionEntity.builder()
                .score(dto.getScore())
                .optionNumber(optionNumber)
                .jobTestId(jobtestId)
                .questionId(dto.getQuestionId())
                .build();
    }

    public static CreateJobtestQuestionResponseDTO toCreateDto(JobtestQuestionEntity entity) {
        return CreateJobtestQuestionResponseDTO.builder()
                .id(entity.getId())
                .score(entity.getScore())
                .optionNumber(entity.getOptionNumber())
                .jobTestId(entity.getJobTestId())
                .questionId(entity.getQuestionId())
                .build();
    }

    public static UpdateJobtestQuestionCommandDTO toUpdateDto(JobtestQuestionEntity entity) {
        return UpdateJobtestQuestionCommandDTO.builder()
                .score(entity.getScore())
                .optionNumber(entity.getOptionNumber())
                .build();
    }
}
