package com.piveguyz.empickbackend.employment.jobtest.command.application.service;

import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.DeleteQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtest.command.application.dto.UpdateQuestionCommandDTO;

public interface QuestionCommandService {
    CreateQuestionCommandDTO createQuestion(CreateQuestionCommandDTO createQuestionCommandDTO);

    UpdateQuestionCommandDTO updateQuestion(int id, UpdateQuestionCommandDTO updateQuestionCommandDTO);

    DeleteQuestionCommandDTO deleteQuestion(int id);
}
