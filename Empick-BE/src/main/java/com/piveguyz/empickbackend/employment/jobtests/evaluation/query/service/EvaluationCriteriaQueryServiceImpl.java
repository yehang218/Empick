package com.piveguyz.empickbackend.employment.jobtests.evaluation.query.service;

import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.mapper.EvaluationCriteriaMapper;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.dto.EvaluationCriteriaQueryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationCriteriaQueryServiceImpl implements EvaluationCriteriaQueryService {
    private final EvaluationCriteriaMapper evaluationCriteriaMapper;

    public EvaluationCriteriaQueryServiceImpl(EvaluationCriteriaMapper evaluationCriteriaMapper) {
        this.evaluationCriteriaMapper = evaluationCriteriaMapper;
    }

    @Override
    public List<EvaluationCriteriaQueryDTO> getEvaluationCriteriaByJobtestId(int jobtestId) {
        return evaluationCriteriaMapper.findByJobtestId(jobtestId);
    }

    @Override
    public EvaluationCriteriaQueryDTO getEvaluationCriteriaById(int id) {
        return evaluationCriteriaMapper.findById(id);
    }
}
