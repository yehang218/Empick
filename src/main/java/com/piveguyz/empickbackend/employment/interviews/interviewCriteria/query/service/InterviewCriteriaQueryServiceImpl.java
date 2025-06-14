package com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto.InterviewCriteriaQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.mapper.InterviewCriteriaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewCriteriaQueryServiceImpl implements InterviewCriteriaQueryService {
    private final InterviewCriteriaMapper mapper;

    @Override
    public List<InterviewCriteriaQueryDTO> findAll() {
        List<InterviewCriteriaQueryDTO> dtoList = mapper.findAll();
        return dtoList;
    }

    @Override
    public InterviewCriteriaQueryDTO findById(Integer id) {
        InterviewCriteriaQueryDTO dto = mapper.findById(id);
        if(dto == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEW_CRITERIA_NOT_FOUND);
        }
        return dto;
    }

    @Override
    public List<InterviewCriteriaQueryDTO> searchByTitle(String title) {
        List<InterviewCriteriaQueryDTO> dtoList = mapper.searchByTitle(title);
        return dtoList;
    }
}
