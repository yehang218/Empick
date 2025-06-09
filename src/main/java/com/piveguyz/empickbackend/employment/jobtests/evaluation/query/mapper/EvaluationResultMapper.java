package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.mapper;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.JobTestEvaluationDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EvaluationResultMapper {
    JobTestEvaluationDetailDTO findEvaluationByApplicationId(@Param("applicationId") int applicationId);
}
