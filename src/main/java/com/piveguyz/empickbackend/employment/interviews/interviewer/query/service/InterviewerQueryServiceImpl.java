package com.piveguyz.empickbackend.employment.interviews.interviewer.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto.InterviewCriteriaQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.mapper.InterviewCriteriaMapper;
import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.service.InterviewCriteriaQueryService;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.dto.InterviewerQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.mapper.InterviewerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterviewerQueryServiceImpl implements InterviewerQueryService {
    private final InterviewerMapper mapper;

    @Override
    public List<InterviewerQueryDTO> findAll() {
        List<InterviewerQueryDTO> dtoList = mapper.findAll();
        return dtoList;
    }

    @Override
    public InterviewerQueryDTO findById(Integer id) {
        InterviewerQueryDTO dto = mapper.findById(id);
        if(dto == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INTERVIEWER_NOT_FOUND);
        }
        return dto;
    }

    @Override
    public List<InterviewerQueryDTO> findByInterviewId(Integer interviewId) {
        List<InterviewerQueryDTO> dtoList = mapper.findByInterviewId(interviewId);
        return dtoList;
    }

    @Override
    public InterviewerQueryDTO findByInterviewMemberId(Integer interviewId, Integer memberId) {
        InterviewerQueryDTO dto = mapper.findByInterviewMemberId(interviewId, memberId);
        return dto;
    }
}
