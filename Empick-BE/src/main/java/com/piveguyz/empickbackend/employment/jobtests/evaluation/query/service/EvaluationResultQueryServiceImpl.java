package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.JobTestEvaluationDetailDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.mapper.EvaluationResultMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationResultQueryServiceImpl implements EvaluationResultQueryService {

    private final EvaluationResultMapper evaluationResultMapper;

    public EvaluationResultQueryServiceImpl(EvaluationResultMapper evaluationResultMapper) {
        this.evaluationResultMapper = evaluationResultMapper;
    }

    @Override
    public List<JobTestEvaluationDetailDTO> getEvaluationResultByApplicationId(int applicationId) {
        List<JobTestEvaluationDetailDTO> dto = evaluationResultMapper.findEvaluationByApplicationId(applicationId);
        if (dto == null) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_EVALUATION_RESULT);
        }
        return dto;
    }
}
