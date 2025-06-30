package com.piveguyz.empickbackend.employment.interviews.interviewer.query.mapper;

import com.piveguyz.empickbackend.employment.interviews.interviewCriteria.query.dto.InterviewCriteriaQueryDTO;
import com.piveguyz.empickbackend.employment.interviews.interviewer.query.dto.InterviewerQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewerMapper {
    List<InterviewerQueryDTO> findAll();

    InterviewerQueryDTO findById(Integer id);

    List<InterviewerQueryDTO> findByInterviewId(Integer interviewId);

    InterviewerQueryDTO findByInterviewMemberId(Integer interviewId, Integer memberId);
}
