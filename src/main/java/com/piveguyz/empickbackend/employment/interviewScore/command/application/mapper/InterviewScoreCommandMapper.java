package com.piveguyz.empickbackend.employment.interviewScore.command.application.mapper;

import com.piveguyz.empickbackend.employment.interviewScore.command.application.dto.InterviewScoreCommandDTO;
import com.piveguyz.empickbackend.employment.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewScoreCommandMapper {
    public InterviewScoreCommandDTO toDto(InterviewScoreEntity entity){
        InterviewScoreCommandDTO dto = new InterviewScoreCommandDTO();
        dto.setId(entity.getId());
        dto.setInterviewId(entity.getInterviewId());
        dto.setInterviewerId(entity.getInterviewerId());
        dto.setItemId(entity.getItemId());
        dto.setScore(entity.getScore());
        dto.setReview(entity.getReview());
        return dto;
    }

    public InterviewScoreEntity toEntity(InterviewScoreCommandDTO dto){
        InterviewScoreEntity entity = new InterviewScoreEntity();
        entity.setId(dto.getId());
        entity.setInterviewId(dto.getInterviewId());
        entity.setInterviewerId(dto.getInterviewerId());
        entity.setItemId(dto.getItemId());
        entity.setScore(dto.getScore());
        entity.setReview(dto.getReview());
        return entity;
    }
}
