package com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.query.service;

import com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.query.dto.InterviewSheetItemQueryDTO;

import java.util.List;

public interface InterviewSheetItemQueryService {
    List<InterviewSheetItemQueryDTO> findAll();

    InterviewSheetItemQueryDTO findById(Integer id);

    List<InterviewSheetItemQueryDTO> findBySheetId(Integer sheetId);

    List<InterviewSheetItemQueryDTO> findByCriteriaId(Integer criteriaId);
}
