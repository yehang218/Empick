package com.piveguyz.empickbackend.employment.interviews.interview.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;
import com.piveguyz.empickbackend.employment.interviews.interview.command.application.mapper.InterviewCommandMapper;
import com.piveguyz.empickbackend.employment.interviews.interview.command.domain.aggregate.InterviewEntity;
import com.piveguyz.empickbackend.employment.interviews.interview.command.domain.repository.InterviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewCommandServiceImpl implements InterviewCommandService {
    private final InterviewRepository repository;
    private final InterviewCommandMapper mapper;

    @Override
    public InterviewCommandDTO create(InterviewCommandDTO dto) {
        InterviewEntity entity = new InterviewEntity();
        entity.setApplicationId(dto.getApplicationId());
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        entity.setInterviewReview(dto.getInterviewReview());
        InterviewEntity createdEntity = repository.save(entity);
        return mapper.toDto(createdEntity);
    }

    @Override
    public InterviewCommandDTO update(Integer id, InterviewCommandDTO dto) {
        InterviewEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setSheetId(dto.getSheetId());
        entity.setDatetime(dto.getDatetime());
        entity.setAddress(dto.getAddress());
        entity.setScore(dto.getScore());
        entity.setInterviewReview(dto.getInterviewReview());
        InterviewEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public InterviewCommandDTO updateDateTime(Integer id, LocalDateTime datetime) {
        InterviewEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setDatetime(datetime);
        InterviewEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public InterviewCommandDTO updateAddress(Integer id, String address) {
        InterviewEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        entity.setAddress(address);
        InterviewEntity updatedEntity = repository.save(entity);
        return mapper.toDto(updatedEntity);
    }

    @Override
    public InterviewCommandDTO delete(Integer id) {
        InterviewEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_NOT_FOUND));
        repository.delete(entity);
        return mapper.toDto(entity);
    }
}
