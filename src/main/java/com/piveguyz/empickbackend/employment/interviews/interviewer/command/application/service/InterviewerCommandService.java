package com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.service;


import com.piveguyz.empickbackend.employment.interviews.interviewer.command.application.dto.InterviewerCommandDTO;

public interface InterviewerCommandService {
    InterviewerCommandDTO createInterviewer(InterviewerCommandDTO dto);

    InterviewerCommandDTO updateInterviewerScore(Integer id, Double score);

    InterviewerCommandDTO updateInterviewerReview(Integer id, String review);

    InterviewerCommandDTO deleteInterviewer(Integer id);
}
