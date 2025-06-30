package com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.mapper;

import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.dto.InterviewScoreCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewScoreCommandMapper {
    public InterviewScoreCommandDTO toDto(InterviewScoreEntity entity) {
        InterviewScoreCommandDTO dto = new InterviewScoreCommandDTO();
        dto.setId(entity.getId());
        dto.setInterviewerId(entity.getInterviewerId());
        dto.setCriteriaId(entity.getCriteriaId());
        dto.setScore(entity.getScore());
        dto.setReview(entity.getReview());
        return dto;
    }

    public InterviewScoreEntity toEntity(InterviewScoreCommandDTO dto) {
        InterviewScoreEntity entity = new InterviewScoreEntity();
        entity.setId(dto.getId());
        entity.setInterviewerId(dto.getInterviewerId());
        entity.setCriteriaId(dto.getCriteriaId());
        entity.setScore(dto.getScore());
        entity.setReview(dto.getReview());
        return entity;
    }
}
