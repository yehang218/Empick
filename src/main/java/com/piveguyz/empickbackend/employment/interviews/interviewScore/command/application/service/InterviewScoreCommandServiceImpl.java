package com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.dto.InterviewScoreCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.application.mapper.InterviewScoreCommandMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.aggregate.InterviewScoreEntity;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.command.domain.repository.InterviewScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewScoreCommandServiceImpl implements InterviewScoreCommandService {
    private final InterviewScoreRepository repository;
    private final InterviewScoreCommandMapper mapper;

    @Override
    public InterviewScoreCommandDTO create(InterviewScoreCommandDTO dto) {
        Integer interviewerId = dto.getInterviewerId();
        Integer itemId = dto.getItemId();
        if(repository.existsByInterviewerIdAndItemId(interviewerId, itemId)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_ALREADY_EXIST);
        }
        Integer score = dto.getScore();
        if(score == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NO_SCORE);
        }
        String review = dto.getReview();
        if(review == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NO_REVIEW);
        }
        InterviewScoreEntity entity = new InterviewScoreEntity();
        entity.setInterviewerId(dto.getInterviewerId());
        entity.setItemId(dto.getItemId());
        entity.setScore(dto.getScore());
        entity.setReview(dto.getReview());
        InterviewScoreEntity createdEntity = repository.save(entity);
        return mapper.toDto(createdEntity);
    }

    @Override
    public InterviewScoreCommandDTO update(Integer id, InterviewScoreCommandDTO dto) {
        Integer score = dto.getScore();
        if(score == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NO_SCORE);
        }
        String review = dto.getReview();
        if(review == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NO_REVIEW);
        }
        InterviewScoreEntity entity = repository.findById(id)
                        .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NOT_FOUND));
        entity.setScore(dto.getScore());
        entity.setReview(dto.getReview());
        InterviewScoreEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public InterviewScoreCommandDTO delete(Integer id) {
        InterviewScoreEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDto(entity);
    }
}
