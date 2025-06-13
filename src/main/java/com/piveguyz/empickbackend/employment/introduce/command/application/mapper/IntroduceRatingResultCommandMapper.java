package com.piveguyz.empickbackend.employment.introduce.command.application.mapper;

import com.piveguyz.empickbackend.employment.introduce.command.application.dto.IntroduceRatingResultCommandDTO;
import com.piveguyz.empickbackend.employment.introduce.command.domain.aggregate.IntroduceRatingResultEntity;

public class IntroduceRatingResultCommandMapper {

    // DTO → Entity (등록용)
    public static IntroduceRatingResultEntity toEntity(IntroduceRatingResultCommandDTO dto) {
        if (dto == null) return null;

        return IntroduceRatingResultEntity.builder()
                .content(dto.getContent())
                .ratingScore(dto.getRatingScore())
                .status(dto.getStatus())
                .updatedAt(dto.getUpdatedAt())
                .updatedBy(dto.getUpdatedBy())
                .memberId(dto.getMemberId())
                .introduceStandardId(dto.getIntroduceStandardId())
                .build();
    }

    // Entity → DTO (조회 응답용)
    public static IntroduceRatingResultCommandDTO toDto(IntroduceRatingResultEntity entity) {
        if (entity == null) return null;

        return IntroduceRatingResultCommandDTO.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .ratingScore(entity.getRatingScore())
                .status(entity.getStatus())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .memberId(entity.getMemberId())
                .introduceStandardId(entity.getIntroduceStandardId())
                .build();
    }

    // 수정용: 기존 Entity에 DTO 내용 반영
    public static void updateEntityFromDto(IntroduceRatingResultCommandDTO dto, IntroduceRatingResultEntity entity) {
        if (dto == null || entity == null) return;

        if (dto.getContent() != null) entity.setContent(dto.getContent());
        if (dto.getRatingScore() != null) entity.setRatingScore(dto.getRatingScore());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getUpdatedAt() != null) entity.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getUpdatedBy() != null) entity.setUpdatedBy(dto.getUpdatedBy());
        if (dto.getMemberId() != null) entity.setMemberId(dto.getMemberId());
        if (dto.getIntroduceStandardId() != null) entity.setIntroduceStandardId(dto.getIntroduceStandardId());
    }
}
