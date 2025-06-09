package com.piveguyz.empickbackend.employment.jobtests.answer.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.CreateGradingResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.dto.UpdateGradingResultCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.aggregate.GradingResultEntity;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.domain.repository.GradingResultRepository;
import com.piveguyz.empickbackend.employment.jobtests.answer.command.application.mapper.GradingResultMapper;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.repository.GradingCriteriaRepository;
import org.springframework.stereotype.Service;

@Service
public class GradingResultCommandServiceImpl implements GradingResultCommandService {

    private final GradingResultRepository gradingResultRepository;
    private final GradingCriteriaRepository criteriaRepository;

    public GradingResultCommandServiceImpl(GradingResultRepository gradingResultRepository,
                                           GradingCriteriaRepository criteriaRepository) {
        this.gradingResultRepository = gradingResultRepository;
        this.criteriaRepository = criteriaRepository;
    }

    // 채점 결과 등록
    @Override
    public CreateGradingResultCommandDTO createGradingResult(CreateGradingResultCommandDTO createGradingResultCommandDTO) {
        // 없는 답안일 때
        if (!gradingResultRepository.existsById(createGradingResultCommandDTO.getAnswerId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_ANSWER);
        }

        // 없는 채점 기준일 때
        if (!criteriaRepository.existsById(createGradingResultCommandDTO.getQuestionGradingCriteriaId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION_GRADING_CRITERIA);
        }

        // Y, N이 아닐 때
        String isSatisfiedStr = createGradingResultCommandDTO.getIsSatisfied();
        if (!"Y".equalsIgnoreCase(isSatisfiedStr) && !"N".equalsIgnoreCase(isSatisfiedStr)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_SATISFIED_VALUE);
        }

        GradingResultEntity entity = GradingResultMapper.toEntity(createGradingResultCommandDTO);
        GradingResultEntity saved = gradingResultRepository.save(entity);

        return GradingResultMapper.toCommandDTO(saved);
    }

    // 채점 결과 수정
    @Override
    public UpdateGradingResultCommandDTO updateGradingResult(int id, UpdateGradingResultCommandDTO updateGradingResultCommandDTO) {
        // 없는 채점 결과일 때
        GradingResultEntity gradingResult = gradingResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_ANSWER));

        // Y, N이 아닐 때
        String isSatisfiedStr = updateGradingResultCommandDTO.getIsSatisfied();
        if (!"Y".equalsIgnoreCase(isSatisfiedStr) && !"N".equalsIgnoreCase(isSatisfiedStr)) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_INVALID_SATISFIED_VALUE);
        }
        
        gradingResult.updateGradingCriteria(updateGradingResultCommandDTO);
        GradingResultEntity updatedEntity = gradingResultRepository.save(gradingResult);
        return GradingResultMapper.toUpdateDto(updatedEntity);
    }

    // 채점 결과 삭제
    @Override
    public int deleteGradingResult(int id) {
        GradingResultEntity gradingResult = gradingResultRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_JOBTEST_ANSWER));
        gradingResultRepository.delete(gradingResult);
        return gradingResult.getId();
    }
}
