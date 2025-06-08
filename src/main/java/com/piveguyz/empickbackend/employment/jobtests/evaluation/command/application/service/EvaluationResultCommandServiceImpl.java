package com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.CreateEvaluationResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.application.dto.UpdateEvaluationResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.aggregate.EvaluationResultEntity;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.repository.EvaluationCriteriaRepository;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.command.domain.repository.EvaluationResultRepository;
import com.piveguyz.empickbackend.employment.jobtests.evaluation.query.mapper.EvaluationResultMapper;
import org.springframework.stereotype.Service;

@Service
public class EvaluationResultCommandServiceImpl implements EvaluationResultCommandService {
    private final EvaluationResultRepository evaluationResultRepository;
//    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    public EvaluationResultCommandServiceImpl(
//            EvaluationCriteriaRepository evaluationCriteriaRepository,
                                              EvaluationResultRepository evaluationResultRepository) {
//        this.evaluationCriteriaRepository = evaluationCriteriaRepository;
        this.evaluationResultRepository = evaluationResultRepository;
    }


    // 실무테스트 평가 결과 등록
    @Override
    public CreateEvaluationResultCommandDTO createEvaluationResult(CreateEvaluationResultCommandDTO createEvaluationResultCommandDTO) {
        // 🚩 존재하지 않는 지원서별 실무테스트인 경우


        // 🚩 존재하지 않는 평가 기준인 경우 (PR 후 합쳐야 함)
//        if (!evaluationCriteriaRepository.existsById((createEvaluationResultCommandDTO.getJobtestEvaluationCriteriaId()))) {
//            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_EVALUATION_CRITERIA);
//        }

        EvaluationResultEntity evaluationResultEntity = EvaluationResultMapper.toEntity(createEvaluationResultCommandDTO);
        EvaluationResultEntity saved = evaluationResultRepository.save(evaluationResultEntity);

        return EvaluationResultMapper.toCreateDto(saved);
    }

    @Override
    public UpdateEvaluationResultCommandDTO updateEvaluationResult(int id, UpdateEvaluationResultCommandDTO updateEvaluationResultCommandDTO) {
        // 평가 결과가 있는지 확인
        EvaluationResultEntity evaluationResultEntity = evaluationResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_EVALUATION_RESULT));

        evaluationResultEntity.updateEvaluationResult(updateEvaluationResultCommandDTO);
        EvaluationResultEntity saved = evaluationResultRepository.save(evaluationResultEntity);

        return EvaluationResultMapper.toUpdateDto(saved);
    }

    @Override
    public int deleteEvaluationResult(int id) {
        // 평가 결과가 있는지 확인
        EvaluationResultEntity evaluationResultEntity = evaluationResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_EVALUATION_RESULT));
        evaluationResultRepository.delete(evaluationResultEntity);
        return evaluationResultEntity.getId();
    }
}
