package com.piveguyz.empickbackend.employment.interviews.interview.query.service;

import com.piveguyz.empickbackend.employment.interviews.interview.query.dto.InterviewQueryDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface InterviewQueryService {
    List<InterviewQueryDTO> findAll();

    InterviewQueryDTO findById(Integer id);

    InterviewQueryDTO findByApplicationId(Integer applicationId);

    List<InterviewQueryDTO> findByDate(LocalDate date);

    Boolean checkAvailable(LocalDateTime datetime);
}
