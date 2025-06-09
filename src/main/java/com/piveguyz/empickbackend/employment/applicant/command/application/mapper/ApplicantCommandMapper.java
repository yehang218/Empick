
package com.piveguyz.empickbackend.employment.applicant.command.application.mapper;

import com.piveguyz.empickbackend.employment.applicant.command.application.dto.ApplicantCommandDTO;
import com.piveguyz.empickbackend.employment.applicant.command.domain.aggregate.ApplicantEntity;

public class ApplicantCommandMapper {

    public static ApplicantEntity toEntity(ApplicantCommandDTO dto) {
        return ApplicantEntity.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .profileUrl(dto.getProfileUrl())
                .birth(dto.getBirth())
                .address(dto.getAddress())
                .build();
    }

    public static ApplicantCommandDTO toDto(ApplicantEntity entity) {
        return ApplicantCommandDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .profileUrl(entity.getProfileUrl())
                .birth(entity.getBirth())
                .address(entity.getAddress())
                .build();
    }
}
