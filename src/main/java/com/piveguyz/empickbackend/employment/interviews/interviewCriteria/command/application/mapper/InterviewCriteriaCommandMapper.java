package com.piveguyz.empickbackend.employment.interviews.interviewCriteria.command.application.mapper;

import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.command.application.dto.InterviewCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.command.domain.aggregate.InterviewCriteriaEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewCriteriaCommandMapper {
    public InterviewCriteriaCommandDTO toDTO(InterviewCriteriaEntity entity) {
        InterviewCriteriaCommandDTO dto = new InterviewCriteriaCommandDTO();

        dto.setId(entity.getId());
        dto.setSheetId(entity.getSheetId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setWeight(entity.getWeight());
        dto.setIsDeleted(entity.getIsDeleted());
        dto.setMemberId(entity.getMemberId());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public InterviewCriteriaEntity toEntity(InterviewCriteriaCommandDTO dto) {
        InterviewCriteriaEntity entity = new InterviewCriteriaEntity();

        entity.setId(dto.getId());
        entity.setSheetId(dto.getSheetId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setWeight(dto.getWeight());
        entity.setIsDeleted(dto.getIsDeleted());
        entity.setMemberId(dto.getMemberId());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }
}
