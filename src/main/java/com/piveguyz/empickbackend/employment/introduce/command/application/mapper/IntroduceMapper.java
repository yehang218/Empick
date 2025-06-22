package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;


import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceEntity;

public class IntroduceMapper {

    public static IntroduceEntity toEntity(IntroduceCommandDTO dto) {
        return IntroduceEntity.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .introduceTemplateId(dto.getIntroduceTemplateId())
                .applicantId(dto.getApplicantId())
                .applicationId(dto.getApplicationId())
                .build();
    }

    public static IntroduceCommandDTO toDTO(IntroduceEntity entity) {
        return IntroduceCommandDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .introduceTemplateId(entity.getIntroduceTemplateId())
                .applicantId(entity.getApplicantId())
                .applicationId(entity.getApplicationId())
                .build();
    }
}
