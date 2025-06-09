package com.piveguyz.empickbackend.employment.interviews.interview.command.application.mapper;

import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interview.command.domain.aggregate.InterviewEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewCommandMapper {
    public InterviewCommandDTO toDto(InterviewEntity entity){
        InterviewCommandDTO dto = new InterviewCommandDTO();
        dto.setId(entity.getId());
        dto.setSheetId(entity.getSheetId());
        dto.setDate(entity.getDate());
        dto.setAddress(entity.getAddress());
        dto.setScore(entity.getScore());
        dto.setInterviewReview(entity.getInterviewReview());
        return dto;
    }

    public InterviewEntity toEntity(InterviewCommandDTO dto){
        InterviewEntity entity = new InterviewEntity();
        entity.setId(dto.getId());
        entity.setSheetId(dto.getSheetId());
        entity.setDate(dto.getDate());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        entity.setInterviewReview(dto.getInterviewReview());
        return entity;
    }
}
