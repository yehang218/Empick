package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceStandardCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceStandardEntity;

public class IntroduceStandardCommandMapper {

    public static IntroduceStandardEntity toEntity(IntroduceStandardCommandDTO dto) {
        return IntroduceStandardEntity.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .memberId(dto.getMemberId())
                .introduceId(dto.getIntroduceId())
                .build();
    }

    public static IntroduceStandardCommandDTO toDTO(IntroduceStandardEntity entity) {
        return IntroduceStandardCommandDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .memberId(entity.getMemberId())
                .introduceId(entity.getIntroduceId())
                .build();
    }
}
