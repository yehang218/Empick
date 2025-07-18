package com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.dto.InterviewerCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.mapper.InterviewerCommandMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.aggregate.InterviewerEntity;
import com.piveguyz.empickbackend.employment.interviews.interviewer.command.domain.repository.InterviewerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewerCommandServiceImpl implements InterviewerCommandService {
    private final InterviewerCommandMapper mapper;
    private final InterviewerRepository repository;

    @Override
    public InterviewerCommandDTO createInterviewer(InterviewerCommandDTO dto) {
        Integer interviewId = dto.getInterviewId();
        Integer memberId = dto.getMemberId();
        if(repository.existsByInterviewIdAndMemberId(interviewId, memberId)){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_ALREADY_EXIST);
        }
        InterviewerEntity entity = new InterviewerEntity();
        entity.setId(dto.getId());
        entity.setInterviewId(dto.getInterviewId());
        entity.setMemberId(dto.getMemberId());
        InterviewerEntity savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public InterviewerCommandDTO updateInterviewerScore(Integer id, Double score) {
        InterviewerEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_NOT_FOUND));
        entity.setScore(score);
        InterviewerEntity savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public InterviewerCommandDTO updateInterviewerReview(Integer id, String review) {
        InterviewerEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_NOT_FOUND));
        entity.setReview(review);
        InterviewerEntity savedEntity = repository.save(entity);
        return mapper.toDTO(savedEntity);
    }

    @Override
    public InterviewerCommandDTO deleteInterviewer(Integer id) {
        InterviewerEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDTO(entity);
    }
}
