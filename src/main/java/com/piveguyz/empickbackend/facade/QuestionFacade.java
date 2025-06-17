package com.piveguyz.empickbackend.facade;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.dto.CreateGradingCriteriaCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.grading.command.application.service.GradingCriteriaCommandService;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionResponse;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.mapper.QuestionMapper;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.service.QuestionCommandService;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.service.QuestionOptionCommandService;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionFacade {
    private final QuestionCommandService questionCommandService;
    private final QuestionOptionCommandService optionCommandService;
    private final GradingCriteriaCommandService gradingCommandService;

    public CreateQuestionResponseDTO createQuestionWithDetails(CreateQuestionCommandDTO dto) {
        int questionId = questionCommandService.createQuestion(dto);

        List<CreateQuestionOptionResponse> options = new ArrayList<>();
        if (dto.getQuestionOptions() != null) {
            for (CreateQuestionOptionCommandDTO option : dto.getQuestionOptions()) {
                options.add(optionCommandService.createQuestionOption(option, questionId));
            }
        }

        List<CreateGradingCriteriaCommandDTO> gradingCriteria = new ArrayList<>();
        if (dto.getGradingCriteria() != null) {
            for (CreateGradingCriteriaCommandDTO grading : dto.getGradingCriteria()) {
                gradingCriteria.add(gradingCommandService.createGradingCriteria(grading, questionId));
            }
        }

        QuestionEntity question = questionCommandService.findById(questionId).orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_INVALID_QUESTION));
        return QuestionMapper.toResponseDto(question, options, gradingCriteria);
    }
}
