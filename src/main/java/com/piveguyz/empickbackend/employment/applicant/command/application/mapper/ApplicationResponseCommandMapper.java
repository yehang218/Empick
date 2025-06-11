package com.piveguyz.empickbackend.employment.applicant.command.application.mapper;


import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicationResponseCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicationResponseEntity;

public class ApplicationResponseCommandMapper {

    public static ApplicationResponseEntity toEntity(ApplicationResponseCommandDTO dto) {
        return ApplicationResponseEntity.builder()
                .applicationId(dto.getApplicationId())
                .applicationItemId(dto.getApplicationItemId())
                .content(dto.getContent())
                .build();
    }

    public static ApplicationResponseCommandDTO toDto(ApplicationResponseEntity entity) {
        return ApplicationResponseCommandDTO.builder()
                .applicationId(entity.getApplicationId())
                .applicationItemId(entity.getApplicationItemId())
                .content(entity.getContent())
                .build();
    }
}
