package com.piveguyz.empickbackend.employment.jobtest.command.application.service;

import com.piveguyz.empickbackend.common.exception.BusinessException;
import com.piveguyz.empickbackend.common.response.ResponseCode;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.DeleteQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.UpdateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.mapper.QuestionMapper;
import com.piveguyz.empickbackend.employment.jobtest.command.domain.aggregate.QuestionEntity;
import com.piveguyz.empickbackend.employment.jobtest.command.domain.repository.QuestionRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionCommandServiceImpl implements QuestionCommandService {

    private final QuestionRepository questionRepository;

    public QuestionCommandServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 실무 테스트 문제 등록
    @Override
    public CreateQuestionCommandDTO createQuestion(CreateQuestionCommandDTO createQuestionCommandDTO) {
        if (questionRepository.existsByContent(createQuestionCommandDTO.getContent())) {
            throw new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_DUPLICATE);
        }

        QuestionEntity questionEntity = QuestionMapper.toEntity(createQuestionCommandDTO);
        QuestionEntity saved = questionRepository.save(questionEntity);

        return QuestionMapper.toCreateDto(saved);
    }


    // 실무 테스트 문제 수정
    @Override
    public UpdateQuestionCommandDTO updateQuestion(UpdateQuestionCommandDTO updateQuestionCommandDTO) {
        QuestionEntity question = questionRepository.findById(updateQuestionCommandDTO.getId())
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_NOT_FOUND));

        question.updateQuestionEntity(updateQuestionCommandDTO);

        QuestionEntity updatedEntity = questionRepository.save(question);

        return QuestionMapper.toUpdateDto(updatedEntity);
    }

    // 실무 테스트 문제 삭제
    @Override
    public DeleteQuestionCommandDTO deleteQuestion(int id) {
        QuestionEntity question = questionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResponseCode.EMPLOYMENT_QUESTION_NOT_FOUND));

        questionRepository.delete(question);

        return QuestionMapper.toDeleteDto(question);
    }
}
