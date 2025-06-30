package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.CreateEvaluationCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.mapper.EvaluationCriteriaMapper;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate.EvaluationCriteriaEntity;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.repository.EvaluationCriteriaRepository;
import com.piveguyz.empickbackend.employment.jobtests.jobtest.command.domain.repository.JobtestRepository;
import org.springframework.stereotype.Service;

@Service
public class EvaluationCriteriaCommandServiceImpl implements EvaluationCriteriaCommandService {
    private final JobtestRepository jobtestRepository;
    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    public EvaluationCriteriaCommandServiceImpl(JobtestRepository jobtestRepository,
                                                EvaluationCriteriaRepository evaluationCriteriaRepository) {
        this.jobtestRepository = jobtestRepository;
        this.evaluationCriteriaRepository = evaluationCriteriaRepository;
    }
    
    // 실무테스트 평가 기준 등록
    @Override
    public CreateEvaluationCriteriaCommandDTO createEvaluationCriteria(CreateEvaluationCriteriaCommandDTO createEvaluationCriteriaCommandDTO) {
        // 존재하지 않는 실무테스트인 경우
        if(!jobtestRepository.existsById(createEvaluationCriteriaCommandDTO.getJobtestId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST);
        }

        // 가중치가 0 ~ 1이 아닌 경우
        if(createEvaluationCriteriaCommandDTO.getScoreWeight() > 1.0f ||
        createEvaluationCriteriaCommandDTO.getScoreWeight() < 0.0f) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_EVALUATION_CRITERIA_OVER_WEIGHT);
        }

        EvaluationCriteriaEntity evaluationCriteriaEntity = EvaluationCriteriaMapper.toEntity(createEvaluationCriteriaCommandDTO);
        EvaluationCriteriaEntity saved = evaluationCriteriaRepository.save(evaluationCriteriaEntity);

        return EvaluationCriteriaMapper.toCreateDto(saved);
    }

    // 실무테스트 평가 기준 수정
    @Override
    public UpdateEvaluationCriteriaCommandDTO updateEvaluationCriteriaCommandDTO(int id, UpdateEvaluationCriteriaCommandDTO updateEvaluationCriteriaCommandDTO) {
        // 평가 기준이 있는지 확인
        EvaluationCriteriaEntity evaluationCriteria = evaluationCriteriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_EVALUATION_CRITERIA));

        // 가중치가 0 ~ 1이 아닌 경우
        if(updateEvaluationCriteriaCommandDTO.getScoreWeight() > 1.0f ||
                updateEvaluationCriteriaCommandDTO.getScoreWeight() < 0.0f) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_EVALUATION_CRITERIA_OVER_WEIGHT);
        }

        evaluationCriteria.updateEvaluationCriteria(updateEvaluationCriteriaCommandDTO);
        EvaluationCriteriaEntity updatedEntity = evaluationCriteriaRepository.save(evaluationCriteria);

        return EvaluationCriteriaMapper.toUpdateDto(updatedEntity);
    }

    // 실무테스트 평가 기준 삭제
    @Override
    public Integer deleteEvaluationCriteria(int id) {
        EvaluationCriteriaEntity evaluationCriteria = evaluationCriteriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_EVALUATION_CRITERIA));
        evaluationCriteriaRepository.delete(evaluationCriteria);
        return evaluationCriteria.getId();
    }
}
