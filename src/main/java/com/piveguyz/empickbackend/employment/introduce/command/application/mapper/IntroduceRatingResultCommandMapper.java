package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultEntity;
import org.springframework.stereotype.Component;

@Component

public class IntroduceRatingResultCommandMapper {

    public IntroduceRatingResultCommandDTO toDTO(IntroduceRatingResultEntity entity) {
        if (entity == null) return null;

        return IntroduceRatingResultCommandDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .ratingScore(entity.getRatingScore())
                .status(entity.getStatus())
                .statusLabel(entity.getStatus().getLabel()) // 프론트 출력용 라벨 포함
                .updatedBy(entity.getUpdatedBy())
                .memberId(entity.getMemberId())
                .introduceStandardId(entity.getIntroduceStandardId())
                .build();
    }

    public IntroduceRatingResultEntity toEntity(IntroduceRatingResultCommandDTO dto) {
        if (dto == null) return null;

        return IntroduceRatingResultEntity.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .ratingScore(dto.getRatingScore())
                .status(dto.getStatus())
                .updatedBy(dto.getUpdatedBy())
                .memberId(dto.getMemberId())
                .introduceStandardId(dto.getIntroduceStandardId())
                .build();
    }
}
