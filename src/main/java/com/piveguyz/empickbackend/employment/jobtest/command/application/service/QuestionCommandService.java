package com.piveguyz.empickbackend.employment.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;

public interface QuestionCommandService {
    CreateQuestionCommandDTO createQuestion(CreateQuestionCommandDTO createQuestionCommandDTO);
}
