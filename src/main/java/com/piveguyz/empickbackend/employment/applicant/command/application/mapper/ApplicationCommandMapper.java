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
            // updatedAt, updatedBy는 등록 시에는 null
            return entity;
        }

        // Entity → DTO
        public static ApplicationCommandDTO toDTO(ApplicationEntity entity) {
            ApplicationCommandDTO dto = new ApplicationCommandDTO();
            dto.setId(entity.getId());
            dto.setRecruitmentId(entity.getRecruitmentId());
            dto.setApplicantId(entity.getApplicantId());
            dto.setStatus(entity.getStatus());
            dto.setIntroduceRatingResultId(entity.getIntroduceRatingResultId());
            return dto;
        }

        // 상태값 업데이트용
        public static void updated(ApplicationEntity entity, int status, int updatedBy) {
            entity.setStatus(status);
            entity.setUpdatedAt(LocalDateTime.now());
            entity.setUpdatedBy(updatedBy);
        }
    }