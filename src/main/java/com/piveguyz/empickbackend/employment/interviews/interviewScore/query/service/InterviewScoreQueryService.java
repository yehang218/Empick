package com.piveguyz.empickbackend.employment.interviews.interviewScore.query.service;

import com.piveguyz.empickbackend.employment.interviews.interviewScore.query.dto.InterviewScoreQueryDTO;

import java.util.List;

public interface InterviewScoreQueryService {
    List<InterviewScoreQueryDTO> findAll();

    InterviewScoreQueryDTO findById(Integer id);

    List<InterviewScoreQueryDTO> findByInterviewerId(Integer interviewerId);
}
