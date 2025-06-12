package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.JobTestEvaluationDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EvaluationResultMapper {
    List<JobTestEvaluationDetailDTO> findEvaluationByApplicationId(@Param("applicationId") int applicationId);
}
