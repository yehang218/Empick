package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.introduceTemplate.IntroduceTemplateItemCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.introduceTemplateCommand.IntroduceTemplateItemEntity;

public class IntroduceTemplateItemMapper {

    // DTO -> Entity
    public static IntroduceTemplateItemEntity toEntity(IntroduceTemplateItemCommandDTO dto) {
        return IntroduceTemplateItemEntity.builder()
                .title(dto.getTitle())
                .memberId(dto.getMemberId())
                .introduceTemplateId(dto.getIntroduceTemplateId())
                .build();
    }

    // Entity -> DTO (Optional)
    public static IntroduceTemplateItemCommandDTO toDTO(IntroduceTemplateItemEntity entity) {
        IntroduceTemplateItemCommandDTO dto = new IntroduceTemplateItemCommandDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setMemberId(entity.getMemberId());
        dto.setIntroduceTemplateId(entity.getIntroduceTemplateId());
        return dto;
    }
}
