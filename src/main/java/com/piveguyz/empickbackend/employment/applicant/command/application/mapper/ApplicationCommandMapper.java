package com.piveguyz.empickbackend.employment.applicant.command.application.mapper;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicationCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicationEntity;

import java.time.LocalDateTime;

public class ApplicationCommandMapper {

    // DTO → Entity
    public static ApplicationEntity toEntity(ApplicationCommandDTO dto) {
        ApplicationEntity entity = new ApplicationEntity();
        entity.setRecruitmentId(dto.getRecruitmentId());
        entity.setApplicantId(dto.getApplicantId());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setIntroduceRatingResultId(dto.getIntroduceRatingResultId());
        return entity;
    }

    // Entity → DTO (선택적으로 사용)
    public static ApplicationCommandDTO toDTO(ApplicationEntity entity) {
        ApplicationCommandDTO dto = new ApplicationCommandDTO();
        dto.setId(entity.getId());
        dto.setRecruitmentId(entity.getRecruitmentId());
        dto.setApplicantId(entity.getApplicantId());
        dto.setStatus(entity.getStatus());
        dto.setIntroduceRatingResultId(entity.getIntroduceRatingResultId());
        return dto;
    }

    public static void updated(ApplicationEntity entity, int status) {
        entity.setStatus(status);
    }


}
