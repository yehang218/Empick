package com.piveguyz.empickbackend.employment.jobtests.question.command.application.service;

import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.CreateQuestionResponseDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.DeleteQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.application.dto.UpdateQuestionCommandDTO;
import com.piveguyz.empickbackend.employment.jobtests.question.command.domain.aggregate.QuestionEntity;

import java.util.Optional;

public interface QuestionCommandService {
    int createQuestion(CreateQuestionCommandDTO createQuestionCommandDTO);

    UpdateQuestionCommandDTO updateQuestion(int id, UpdateQuestionCommandDTO updateQuestionCommandDTO);

    DeleteQuestionCommandDTO deleteQuestion(int id);

    Optional<QuestionEntity> findById(int questionId);
}
