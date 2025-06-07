package com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.mapper;

import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.CreateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.application.dto.UpdateJobtestCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.aggregate.JobtestEntity;

import java.time.LocalDateTime;

public class JobtestMapper {

    // 실무테스트 생성 DTO -> Entity
    public static JobtestEntity toEntity(CreateJobtestCommandDTO dto) {
        return JobtestEntity.builder()
                .title(dto.getTitle())
                .difficulty(dto.getDifficulty())
                .testTime(dto.getTestTime())
                .createdAt(LocalDateTime.now())
                .createdMemberId(dto.getCreatedMemberId())
                .build();
    }

    // Entity -> 실무테스트 생성 DTO
    public static CreateJobtestCommandDTO toCreateDto(JobtestEntity entity) {
        return CreateJobtestCommandDTO.builder()
                .title(entity.getTitle())
                .difficulty(entity.getDifficulty())
                .testTime(entity.getTestTime())
                .createdMemberId(entity.getCreatedMemberId())
                .build();
    }

    public static UpdateJobtestCommandDTO toUpdateDto(JobtestEntity updatedEntity) {
        return UpdateJobtestCommandDTO.builder()
                .title(updatedEntity.getTitle())
                .difficulty(updatedEntity.getDifficulty())
                .testTime(updatedEntity.getTestTime())
                .updatedMemberId(updatedEntity.getUpdatedMemberId())
                .build();
    }
}
