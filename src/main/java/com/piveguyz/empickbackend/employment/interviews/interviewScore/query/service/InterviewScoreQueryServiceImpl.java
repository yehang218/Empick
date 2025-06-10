package com.piveguyz.empickbackend.employment.interviews.interviewScore.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.dto.InterviewScoreQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.mapper.InterviewScoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewScoreQueryServiceImpl implements InterviewScoreQueryService {
    private final InterviewScoreMapper interviewScoreMapper;

    @Override
    public List<InterviewScoreQueryDTO> findAll() {
        List<InterviewScoreQueryDTO> dtoList = interviewScoreMapper.findAll();
        return dtoList;
    }

    @Override
    public InterviewScoreQueryDTO findById(Integer id) {
        InterviewScoreQueryDTO dto = interviewScoreMapper.findById(id);
        if(dto == null){
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_SCORE_NOT_FOUND);
        }
        return dto;
    }

    @Override
    public List<InterviewScoreQueryDTO> findByInterviewerId(Integer interviewerId) {
        List<InterviewScoreQueryDTO> dtoList = interviewScoreMapper.findByInterviewerId(interviewerId);
        return dtoList;
    }
}
