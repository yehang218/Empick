package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateEntity;

public class IntroduceTemplateCommandMapper {

    public static IntroduceTemplateEntity toEntity(IntroduceTemplateCommandDTO dto) {
        return IntroduceTemplateEntity.builder()
                .title(dto.getTitle())
                .memberId(dto.getMemberId())
                .build();
    }

    public static IntroduceTemplateCommandDTO toDTO(IntroduceTemplateEntity entity) {
        return IntroduceTemplateCommandDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .memberId(entity.getMemberId())
                .build();
    }
}
