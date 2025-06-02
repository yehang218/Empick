package com.piveguyz.empickbackend.employment.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;
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
        return null;
    }
    
    
    // 실무 테스트 문제 수정
    
    
    // 실무 테스트 문제 삭제
}
