package com.piveguyz.empickbackend.employment.interviewScore.command.application.service;


import com.piveguyz.empickbackend.employment.interviewScore.command.application.dto.InterviewScoreCommandDTO;

public interface InterviewScoreCommandService {
    InterviewScoreCommandDTO create(InterviewScoreCommandDTO dto);

    InterviewScoreCommandDTO update(Integer id, InterviewScoreCommandDTO dto);

    InterviewScoreCommandDTO delete(Integer id);
}
