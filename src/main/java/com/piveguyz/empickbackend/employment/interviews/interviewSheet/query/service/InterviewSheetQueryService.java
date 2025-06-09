package com.piveguyz.empickbackend.employment.interviews.interviewSheet.query.service;

import com.piveguyz.empickbackend.employment.interviews.interviewSheet.query.dto.InterviewSheetQueryDTO;

import java.util.List;

public interface InterviewSheetQueryService {
    List<InterviewSheetQueryDTO> findAll();

    InterviewSheetQueryDTO findById(Integer id);

    List<InterviewSheetQueryDTO> searchByName(String name);
}
