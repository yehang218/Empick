package com.piveguyz.empickbackend.employment.interviews.interviewer.query.service;

import com.piveguyz.empickbackend.employment.interviews.interviewer.query.dto.InterviewerQueryDTO;

import java.util.List;

public interface InterviewerQueryService {
    List<InterviewerQueryDTO> findAll();

    InterviewerQueryDTO findById(Integer id);

    List<InterviewerQueryDTO> findByInterviewId(Integer interviewId);
}
