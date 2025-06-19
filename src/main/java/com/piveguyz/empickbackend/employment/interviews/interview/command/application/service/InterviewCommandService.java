package com.piveguyz.empickbackend.employment.interviews.interview.command.application.service;


import com.piveguyz.empickbackend.employment.interviews.interview.command.application.dto.InterviewCommandDTO;

import java.time.LocalDateTime;

public interface InterviewCommandService {
    InterviewCommandDTO create(InterviewCommandDTO dto);

    InterviewCommandDTO update(Integer id, InterviewCommandDTO dto);

    InterviewCommandDTO updateDateTime(Integer id, LocalDateTime datetime);

    InterviewCommandDTO updateAddress(Integer id, String address);

    InterviewCommandDTO updateScore(Integer id, Double score);

    InterviewCommandDTO delete(Integer id);
}
