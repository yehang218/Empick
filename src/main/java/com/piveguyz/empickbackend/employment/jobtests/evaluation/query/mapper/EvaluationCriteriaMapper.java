package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.EvaluationCriteriaQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface EvaluationCriteriaMapper {
    List<EvaluationCriteriaQueryDTO> findByJobtestId(@Param("jobtestId") int jobtestId);

    EvaluationCriteriaQueryDTO findById(@Param("id") int id);
}
