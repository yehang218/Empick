package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceTemplateCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceTemplateEntity;

import java.util.List;

public class IntroduceTemplateCommandMapper {

    public static IntroduceTemplateEntity toEntity(IntroduceTemplateCommandDTO dto) {
        return IntroduceTemplateEntity.builder()
                .title(dto.getTitle())
                .memberId(dto.getMemberId())
                .build();
    }

    // itemIds를 포함해서 DTO로 변환
    public static IntroduceTemplateCommandDTO toDTO(IntroduceTemplateEntity entity, List<Integer> itemIds) {
        return IntroduceTemplateCommandDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .memberId(entity.getMemberId())
                .itemIds(itemIds)
                .build();
    }

    // 기존 방식(호환용)
    public static IntroduceTemplateCommandDTO toDTO(IntroduceTemplateEntity entity) {
        return IntroduceTemplateCommandDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .memberId(entity.getMemberId())
                .build();
    }
}