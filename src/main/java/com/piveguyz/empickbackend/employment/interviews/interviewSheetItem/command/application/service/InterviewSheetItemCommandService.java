package com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.command.application.service;


import com.piveguyz.empickbackend.employment.interviews.interviewSheetItem.command.application.dto.InterviewSheetItemCommandDTO;

public interface InterviewSheetItemCommandService {
    InterviewSheetItemCommandDTO create(InterviewSheetItemCommandDTO interviewSheetItemCommandDTO);

    InterviewSheetItemCommandDTO delete(Integer id);
}
