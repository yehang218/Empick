package com.piveguyz.empickbackend.employment.interviewScore.query.mapper;

import com.piveguyz.empickbackend.employment.interviewScore.query.dto.InterviewScoreQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InterviewScoreMapper {
    List<InterviewScoreQueryDTO> findAll();

    InterviewScoreQueryDTO findById(Integer id);

    List<InterviewScoreQueryDTO> findByInterviewId(Integer interviewId);
}
