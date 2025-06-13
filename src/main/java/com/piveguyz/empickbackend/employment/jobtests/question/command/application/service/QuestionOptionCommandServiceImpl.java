package com.piveguyz.empickbackend.employment.jobtests.question.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionOptionResponse;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionOptionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.mapper.QuestionOptionMapper;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionOptionEntity;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.repository.QuestionOptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionOptionCommandServiceImpl implements QuestionOptionCommandService {

    private final QuestionOptionRepository questionOptionRepository;

    public QuestionOptionCommandServiceImpl(QuestionOptionRepository questionOptionRepository) {
        this.questionOptionRepository = questionOptionRepository;
    }

    // 선택지 등록
    @Override
    public CreateQuestionOptionResponse createQuestionOption(CreateQuestionOptionCommandDTO createQuestionOptionCommandDTO,
                                                             int questionId) {
        int count = questionOptionRepository.countByQuestionId(questionId);
        if (count >= 5) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_OPTION_COUNT_EXCEEDED);
        }

        QuestionOptionEntity entity = QuestionOptionMapper.toEntity(createQuestionOptionCommandDTO, count + 1, questionId);
        QuestionOptionEntity saved = questionOptionRepository.save(entity);

        return QuestionOptionMapper.toCreateResponse(saved);
    }

    // 선택지 수정
    @Override
    public UpdateQuestionOptionCommandDTO updateQuestionOption(int id, UpdateQuestionOptionCommandDTO updateQuestionOptionCommandDTO) {
        QuestionOptionEntity option = questionOptionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_OPTION_NOT_FOUND));

        int optionNumber = updateQuestionOptionCommandDTO.getOptionNumber();
        if (optionNumber > 5) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_OPTION_MAX_NUMBER);
        }

        option.updateQuestionOptionEntity(updateQuestionOptionCommandDTO);
        QuestionOptionEntity optionEntity = questionOptionRepository.save(option);

        return QuestionOptionMapper.toUpdateDto(optionEntity);
    }

    // 선택지 삭제
    @Override
    public Integer deleteQuestionOption(int id) {
        QuestionOptionEntity option = questionOptionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_OPTION_NOT_FOUND));
        questionOptionRepository.delete(option);
        return option.getId();
    }
}
