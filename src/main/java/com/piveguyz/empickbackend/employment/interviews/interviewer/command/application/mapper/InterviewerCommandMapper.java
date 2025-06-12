package com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.mapper;

import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.dto.InterviewerCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.aggregate.InterviewerEntity;
import org.springframework.stereotype.Component;

@Component
public class InterviewerCommandMapper {
    public InterviewerCommandDTO toDTO(InterviewerEntity entity) {
        InterviewerCommandDTO dto = new InterviewerCommandDTO();
        dto.setId(entity.getId());
        dto.setInterviewId(entity.getInterviewId());
        dto.setInterviewerId(entity.getInterviewerId());
        dto.setScore(entity.getScore());
        dto.setReview(entity.getReview());
        return dto;
    }

    public InterviewerEntity toEntity(InterviewerCommandDTO dto) {
        InterviewerEntity entity = new InterviewerEntity();
        entity.setId(dto.getId());
        entity.setInterviewId(dto.getInterviewId());
        entity.setInterviewerId(dto.getInterviewerId());
        entity.setScore(dto.getScore());
        entity.setReview(dto.getReview());
        return entity;
    }
}
