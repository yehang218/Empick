package com.piveguyz.empickbackend.employment.interviewScore.query.service;

import com.piveguyz.empickbackend.employment.interviewScore.query.dto.InterviewScoreQueryDTO;

import java.util.List;

public interface InterviewScoreQueryService {
    List<InterviewScoreQueryDTO> findAll();

    InterviewScoreQueryDTO findById(Integer id);

    List<InterviewScoreQueryDTO> findByInterviewId(Integer interviewId);
}
