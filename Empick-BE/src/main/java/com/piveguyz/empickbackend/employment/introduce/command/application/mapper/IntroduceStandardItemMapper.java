package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardItemCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceStandardItemEntity;

public class IntroduceStandardItemMapper {

    public static IntroduceStandardItemEntity toEntity(IntroduceStandardItemCommandDTO dto) {
        return IntroduceStandardItemEntity.builder()
                .content(dto.getContent())
                .memberId(dto.getMemberId())
                .introduceStandardId(dto.getIntroduceStandardId())
                .build();
    }

    public static IntroduceStandardItemCommandDTO toDTO(IntroduceStandardItemEntity entity) {
        return IntroduceStandardItemCommandDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .memberId(entity.getMemberId())
                .introduceStandardId(entity.getIntroduceStandardId())
                .build();
    }
}
