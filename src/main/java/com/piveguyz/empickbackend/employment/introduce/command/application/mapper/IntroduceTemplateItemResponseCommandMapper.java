package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateItemResponseCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateItemResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class IntroduceTemplateItemResponseCommandMapper {

    public IntroduceTemplateItemResponseEntity toEntity(IntroduceTemplateItemResponseCommandDTO dto) {
        return IntroduceTemplateItemResponseEntity.builder()
                .introduceId(dto.getIntroduceId())
                .introduceTemplateItemId(dto.getIntroduceTemplateItemId())
                .content(dto.getContent())
                .build();
    }

    public IntroduceTemplateItemResponseCommandDTO toDTO(IntroduceTemplateItemResponseEntity entity) {
        return IntroduceTemplateItemResponseCommandDTO.builder()
                .introduceId(entity.getIntroduceId())
                .introduceTemplateItemId(entity.getIntroduceTemplateItemId())
                .content(entity.getContent())
                .build();
    }
}
