package com.piveguyz.empickbackend.employment.interviews.interview.command.application.mapper;

import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interview.command.domain.aggregate.InterviewEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewCommandMapper {
    public InterviewCommandDTO toDto(InterviewEntity entity){
        InterviewCommandDTO dto = new InterviewCommandDTO();
        dto.setId(entity.getId());
        dto.setApplicationId(entity.getApplicationId());
        dto.setSheetId(entity.getSheetId());
        dto.setDatetime(entity.getDatetime());
        dto.setAddress(entity.getAddress());
        dto.setScore(entity.getScore());
        dto.setInterviewReview(entity.getInterviewReview());
        return dto;
    }

    public InterviewEntity toEntity(InterviewCommandDTO dto){
        InterviewEntity entity = new InterviewEntity();
        entity.setId(dto.getId());
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        entity.setInterviewReview(dto.getInterviewReview());
        return entity;
    }
}
