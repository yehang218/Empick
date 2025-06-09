package com.piveguyz.empickbackend.employment.jobtests.grading.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.CreateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.UpdateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.mapper.GradingCriteriaMapper;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.aggregate.GradingCriteriaEntity;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.domain.repository.GradingCriteriaRepository;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class GradingCriteriaCommandServiceImpl implements GradingCriteriaCommandService {

    private final GradingCriteriaRepository gradingCriteriaRepository;
    private final QuestionRepository questionRepository;

    public GradingCriteriaCommandServiceImpl(GradingCriteriaRepository gradingCriteriaRepository,
                                             QuestionRepository questionRepository) {
        this.gradingCriteriaRepository = gradingCriteriaRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public CreateGradingCriteriaCommandDTO createGradingCriteria(CreateGradingCriteriaCommandDTO createGradingCriteriaCommandDTO) {
        // 존재하지 않는 문제인 경우
        if (!questionRepository.existsById(createGradingCriteriaCommandDTO.getQuestionId())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_NOT_FOUND);
        }

        // 가중치가 0 ~ 1이 아닌 경우
        if(createGradingCriteriaCommandDTO.getScoreWeight() > 1.0f ||
                createGradingCriteriaCommandDTO.getScoreWeight() < 0.0f) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_EVALUATION_CRITERIA_OVER_WEIGHT);
        }

        GradingCriteriaEntity gradingCriteriaEntity = GradingCriteriaMapper.toEntity(createGradingCriteriaCommandDTO);
        GradingCriteriaEntity saved = gradingCriteriaRepository.save(gradingCriteriaEntity);

        return GradingCriteriaMapper.toCreateDto(saved);
    }

    @Override
    public UpdateGradingCriteriaCommandDTO updateGradingCriteria(int id, UpdateGradingCriteriaCommandDTO updateGradingCriteriaCommandDTO) {
        // 채점 기준이 있는지 확인
        GradingCriteriaEntity gradingCriteria = gradingCriteriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION_GRADING_CRITERIA));

        // 가중치가 0 ~ 1이 아닌 경우
        if(updateGradingCriteriaCommandDTO.getScoreWeight() > 1.0f ||
                updateGradingCriteriaCommandDTO.getScoreWeight() < 0.0f) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_JOBTEST_EVALUATION_CRITERIA_OVER_WEIGHT);
        }

        gradingCriteria.updateGradingCriteria(updateGradingCriteriaCommandDTO);
        GradingCriteriaEntity updatedEntity = gradingCriteriaRepository.save(gradingCriteria);

        return GradingCriteriaMapper.toUpdateDto(updatedEntity);
    }

    @Override
    public int deleteGradingCriteria(int id) {
        GradingCriteriaEntity gradingCriteria = gradingCriteriaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION_GRADING_CRITERIA));

        gradingCriteriaRepository.delete(gradingCriteria);
        return gradingCriteria.getId();
    }
}
