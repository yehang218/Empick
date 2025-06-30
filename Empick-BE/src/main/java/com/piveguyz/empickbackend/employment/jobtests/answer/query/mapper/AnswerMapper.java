package com.piveguyz.empickbackend.employment.jobtests.answer.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.answer.query.dto.AnswerQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnswerMapper {
    List<AnswerQueryDTO> findAnswerWithApplicationJobtestId(int applicationJobtestId);
}
