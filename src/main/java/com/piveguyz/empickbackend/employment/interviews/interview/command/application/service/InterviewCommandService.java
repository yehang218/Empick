package com.piveguyz.empickbackend.employment.interviews.interview.command.application.service;


import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;

public interface InterviewCommandService {
    InterviewCommandDTO create(InterviewCommandDTO dto);

    InterviewCommandDTO update(Integer id, InterviewCommandDTO dto);

    InterviewCommandDTO delete(Integer id);
}
