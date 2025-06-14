package com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.service;

import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto.InterviewCriteriaQueryDTO;

import java.util.List;

public interface InterviewCriteriaQueryService {
    List<InterviewCriteriaQueryDTO> findAll();

    InterviewCriteriaQueryDTO findById(Integer id);

    List<InterviewCriteriaQueryDTO> searchByTitle(String title);

    List<InterviewCriteriaQueryDTO> findBySheetId(Integer sheetId);
}
